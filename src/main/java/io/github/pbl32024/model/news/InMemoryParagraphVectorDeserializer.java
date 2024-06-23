package io.github.pbl32024.model.news;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.lang.StringUtils;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.VectorsConfiguration;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.shade.guava.base.Charsets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class InMemoryParagraphVectorDeserializer {

    public static ParagraphVectors deserialize(InputStream inputStream) throws IOException {
        ZipInputStream zis = inputStream instanceof ZipInputStream
                ? (ZipInputStream) inputStream
                : new ZipInputStream(inputStream);

        Pair<InMemoryLookupTable, VocabCache> pair = null;
        List<double[]> rawRows = null;
        Map<String, List<Byte>> huffmanCodes = null;
        Map<String, List<Integer>> huffmanPoints = null;
        VectorsConfiguration configuration = null;
        Map<String, List<Double>> frequencyMap = null;
        Set<String> labelStrings = null;

        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            switch (zipEntry.getName()) {
                case "syn0.txt" -> {
                    log.info("Reading syn0.txt");
                    pair = WordVectorSerializer.loadTxt(new CloseShieldInputStream(zis));
                }
                case "syn1.txt" -> {
                    log.info("Reading syn1.txt");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(zis)))) {
                        rawRows = reader.lines()
                                .map(line -> StringUtils.split(line, ' '))
                                .map(InMemoryParagraphVectorDeserializer::toDoubles)
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                }
                case "codes.txt" -> {
                    log.info("Reading codes.txt");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(zis)))) {
                        huffmanCodes = reader.lines()
                                .parallel()
                                .map(line -> spliterate(line, (l, s, e) -> Byte.parseByte(l.substring(s, e))))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }
                }
                case "huffman.txt" -> {
                    log.info("Reading huffman.txt");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(zis)))) {
                        huffmanPoints = reader.lines()
                                .parallel()
                                .map(line -> spliterate(line, (l, s, e) -> Integer.parseInt(l, s, e, 10)))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }
                }
                case "config.json" -> {
                    log.info("Reading config.json");
                    configuration = VectorsConfiguration.fromJson(IOUtils.toString(new CloseShieldInputStream(zis), Charsets.UTF_8));
                }
                case "labels.txt" -> {
                    log.info("Reading labels.txt");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(zis)))) {
                        labelStrings = reader.lines().map(String::trim).map(WordVectorSerializer.ReadHelper::decodeB64).collect(Collectors.toSet());
                    }
                }
                case "frequencies.txt" -> {
                    log.info("Reading frequencies.txt");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(zis)))) {
                        frequencyMap = reader.lines()
                                .parallel()
                                .map(line -> spliterate(line, (l, s, e) -> Double.parseDouble(l.substring(s, e))))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }
                }
                default -> log.info("Skipping {}", zipEntry.getName());
            }
        }

        InMemoryLookupTable lookupTable = pair.getFirst();
        lookupTable.setNegative(configuration.getNegative());
        if (configuration.getNegative() > 0.0) {
            lookupTable.initNegative();
        }
        VocabCache<VocabWord> vocab = (VocabCache)pair.getSecond();

        List<INDArray> rows = rawRows.stream()
                .map(row -> Nd4j.create(row, new long[]{(long)row.length}, lookupTable.getSyn0().dataType()))
                .toList();
        rawRows.clear();
        if (!rows.isEmpty()) {
            lookupTable.setSyn1(Nd4j.vstack(rows));
        }

        huffmanPoints.forEach((key, value) -> vocab.wordFor(key).setPoints(value));
        huffmanPoints.clear();

        huffmanCodes.forEach((key, value) -> {
            VocabWord word = vocab.wordFor(key);
            word.setCodes(value);
            word.setCodeLength((short) value.size());
        });
        huffmanCodes.clear();

        frequencyMap.forEach((key, value) -> {
            VocabWord word = vocab.tokenFor(key);
            word.setElementFrequency(value.get(0).longValue());
            word.setSequencesCount(value.get(1).longValue());
        });
        frequencyMap.clear();

        labelStrings.forEach(label -> vocab.tokenFor(label).markAsLabel(true));
        labelStrings.clear();

        return new ParagraphVectors.Builder(configuration)
                .vocabCache(vocab)
                .lookupTable(lookupTable)
                .resetModel(false)
                .build();
    }

    private static double[] toDoubles(String[] strings) {
        double[] result = new double[strings.length];
        for(int i = 0; i < strings.length; ++i) {
            result[i] = Double.parseDouble(strings[i]);
        }
        return result;
    }

    private static <T> Map.Entry<String, List<T>> spliterate(String line, RangeFunction<T> mapper) {
        int index = line.indexOf(' ');
        String key = WordVectorSerializer.ReadHelper.decodeB64(line.substring(0, index));

        List<T> results = new ArrayList<>();
        while (true) {
            int nextIndex = line.indexOf(' ', index + 1);
            if (nextIndex == -1) {
                results.add(mapper.parse(line, index + 1, line.length()));
                break;
            } else {
                results.add(mapper.parse(line, index + 1, nextIndex));
                index = nextIndex;
            }
        }

        return new AbstractMap.SimpleEntry<>(key, results);
    }

    @FunctionalInterface
    private interface RangeFunction<T> {
        T parse(String line, int begin, int end);
    }

}
