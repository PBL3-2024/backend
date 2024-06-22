package io.github.pbl32024.model.news;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LazyParagraphVectors {

    private final AtomicBoolean ready = new AtomicBoolean(false);
    private final AtomicReference<ParagraphVectors> vectors = new AtomicReference<>();

    private final Resource model;
    private final Set<String> stopWords;

    public LazyParagraphVectors(@Value("${backend.news.model}") Resource newsModel,
                                @Value("${backend.news.stopwords}") Resource stopWords) throws IOException {
        this.model = newsModel;
        this.stopWords = new BufferedReader(new InputStreamReader(stopWords.getInputStream())).lines().collect(Collectors.toSet());
    }

    @PostConstruct
    public void initialize() {
        Thread initThread = new Thread(() -> {
            try {
                ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(model.getInputStream());
                TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
                paragraphVectors.setTokenizerFactory(tokenizerFactory);

                vectors.set(paragraphVectors);
                ready.set(true);

                log.info("News classifier loaded");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        initThread.setDaemon(true);
        initThread.start();
    }

    public boolean isReady() {
        return ready.get();
    }

    public synchronized Map<String, Double> classify(String text, int count) {
        String preprocessedText = preprocess(text);
        ParagraphVectors paragraphVectors = vectors.get();
        return paragraphVectors.nearestLabels(preprocessedText, count).stream().collect(Collectors.toMap(
                Function.identity(),
                soc -> paragraphVectors.similarityToLabel(preprocessedText, soc)
        ));
    }

    private String preprocess(String input) {
        return Arrays.stream(input.toLowerCase(Locale.ENGLISH)
                        .split("\\s+"))
                .map(s -> s.replaceAll("[^a-z]", ""))
                .filter(Predicate.not(stopWords::contains))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }

}
