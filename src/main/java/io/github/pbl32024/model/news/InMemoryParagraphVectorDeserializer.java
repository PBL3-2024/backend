package io.github.pbl32024.model.news;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class InMemoryParagraphVectorDeserializer {

    public static ParagraphVectors deserialize(InputStream inputStream) throws IOException {
        File tmp = null;
        try {
            tmp = File.createTempFile("paragraphVectors", "tmp");
            FileUtils.copyToFile(inputStream, tmp);

            ZipFile zipFile = new ZipFile(tmp);

            log.info("Reading syn0.txt");
            Pair<InMemoryLookupTable, VocabCache> pair;
            ZipEntry syn0txt = zipFile.getEntry("syn0.txt");
            try (InputStream zis = zipFile.getInputStream(syn0txt)) {
                pair = WordVectorSerializer.loadTxt(zis);
            }

            log.info("Reading config.json");
            VectorsConfiguration configuration;
            ZipEntry configJson = zipFile.getEntry("config.json");
            try (InputStream zis = zipFile.getInputStream(configJson)) {
                configuration = VectorsConfiguration.fromJson(IOUtils.toString(zis, Charsets.UTF_8));
            }

            InMemoryLookupTable lookupTable = pair.getFirst();
            lookupTable.setNegative(configuration.getNegative());
            if (configuration.getNegative() > 0.0) {
                lookupTable.initNegative();
            }
            VocabCache<VocabWord> vocab = (VocabCache) pair.getSecond();

            log.info("Reading syn1.txt");
            ZipEntry syn1txt = zipFile.getEntry("syn1.txt");
            try (InputStream zis = zipFile.getInputStream(syn1txt);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(zis, Charsets.UTF_8))) {
                INDArray[] result = reader.lines().map(line -> {
                    double[] values = toDoubles(line);
                    return Nd4j.create(values, new long[]{(long) values.length}, lookupTable.getSyn0().dataType());
                }).toArray(INDArray[]::new);
                if (result.length > 0) {
                    lookupTable.setSyn1(Nd4j.vstack(result));
                }
            }

            log.info("Reading huffman.txt");
            ZipEntry huffmanTxt = zipFile.getEntry("huffman.txt");
            try (InputStream zis = zipFile.getInputStream(huffmanTxt);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(zis, Charsets.UTF_8))) {
                reader.lines()
                        .map(line -> spliterate(line, (l, s, e) -> Integer.parseInt(l, s, e, 10)))
                        .forEach(entry -> vocab.wordFor(entry.getKey()).setPoints(entry.getValue()));
            }

            log.info("Reading codes.txt");
            ZipEntry codesTxt = zipFile.getEntry("codes.txt");
            try (InputStream zis = zipFile.getInputStream(codesTxt);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(zis, Charsets.UTF_8))) {
                reader.lines()
                        .map(line -> spliterate(line, (l, s, e) -> Byte.parseByte(l.substring(s, e))))
                        .forEach(entry -> {
                            VocabWord word = vocab.wordFor(entry.getKey());
                            word.setCodes(entry.getValue());
                            word.setCodeLength((short) entry.getValue().size());
                        });
            }

            log.info("Reading frequency.txt");
            ZipEntry frequenciesTxt = zipFile.getEntry("frequencies.txt");
            try (InputStream zis = zipFile.getInputStream(frequenciesTxt);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(zis, Charsets.UTF_8))) {
                reader.lines()
                        .map(line -> spliterate(line, (l, s, e) -> Double.parseDouble(l.substring(s, e))))
                        .forEach(entry -> {
                            VocabWord word = vocab.tokenFor(entry.getKey());
                            word.setElementFrequency(entry.getValue().get(0).longValue());
                            word.setSequencesCount(entry.getValue().get(1).longValue());
                        });
            }

            log.info("Reading labels.txt");
            ZipEntry labelsTxt = zipFile.getEntry("labels.txt");
            try (InputStream zis = zipFile.getInputStream(labelsTxt);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(zis, Charsets.UTF_8))) {
                reader.lines()
                        .map(line -> WordVectorSerializer.ReadHelper.decodeB64(line.trim()))
                        .forEach(label -> vocab.tokenFor(label).markAsLabel(true));
            }

            return new ParagraphVectors.Builder(configuration)
                    .vocabCache(vocab)
                    .lookupTable(lookupTable)
                    .resetModel(false)
                    .build();
        } finally {
            tmp.delete();
        }
    }

    private static double[] toDoubles(String row) {
        String[] split = StringUtils.split(row, ' ');
        double[] result = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            result[i] = Double.parseDouble(split[i]);
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
