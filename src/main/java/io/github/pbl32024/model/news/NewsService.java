package io.github.pbl32024.model.news;

import io.github.pbl32024.model.ExternalLink;
import io.github.pbl32024.model.occupation.Occupation;
import io.github.pbl32024.model.occupation.OccupationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.SimpleLabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class NewsService {

	private static final String STATE_NEW = "NEW";
	private static final String STATE_CLASSIFIED = "CLASSIFIED";
	private static final String STATE_UNCLASSIFIED = "UNCLASSIFIED";
	private static final double THRESHOLD = 0.35;

	private final NewsDAO newsDAO;
	private final RSSClient rSSClient;
	private final RSSProperties rSSProperties;
	private final LazyParagraphVectors paragraphVectors;
	private final OccupationService occupationService;

	public NewsService(NewsDAO newsDAO,
					   RSSClient rSSClient,
					   RSSProperties rSSProperties,
					   LazyParagraphVectors paragraphVectors,
                       OccupationService occupationService) throws IOException {
		this.newsDAO = newsDAO;
		this.rSSClient = rSSClient;
		this.rSSProperties = rSSProperties;
        this.paragraphVectors = paragraphVectors;
        this.occupationService = occupationService;
	}

	public List<News> getNews(NewsQuery query) {
		return newsDAO.getBySocCode(query.getSocCode());
	}

	@PostConstruct
	public void fetchRSSFeeds() {
		try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
			for (RSSSource source : rSSProperties.getSources()) {
				executorService.submit(() -> {
					RSSFeed feed;
					try {
						 feed = rSSClient.fetchRssFeed(source);
					} catch (Exception e) {
						log.warn("Failed to fetch RSS feed {}", source.getUrl(), e);
						return;
					}

					List<News> newsList = new ArrayList<>();
					for (RSSArticle article : feed.getChannel().getItem()) {
						News news = new News();
						news.setId(article.getLink());
						news.setSource(source.getId());
						news.setPublished(DateTimeFormatter.RFC_1123_DATE_TIME.parse(article.getPubDate(), LocalDateTime::from));
						news.setState(STATE_NEW);
						ExternalLink externalLink = new ExternalLink();
						externalLink.setLabel("Click here to read");
						externalLink.setUrl(article.getLink());
						news.setExternalLink(externalLink);
						news.setTitle(article.getTitle());
						news.setDescription(article.getDescription());
						news.setCategories(article.getCategory());
						newsList.add(news);
					}
					newsDAO.save(newsList);
				});
			}
		}
	}

	@Scheduled(fixedRate = 10, timeUnit = TimeUnit.MILLISECONDS)
	public void classifyNews() {
		if (!paragraphVectors.isReady()) {
			return;
		}

		News news = newsDAO.getNewNews();

		if (news == null) {
			return;
		}

		log.info("Classifying news article {} - {}", news.getId(), news.getTitle());

		String input = Stream.concat(Stream.of(news.getTitle(), news.getDescription()),
						ObjectUtils.defaultIfNull(news.getCategories(), List.<String>of()).stream())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));

		Set<String> socMappings = paragraphVectors.classify(input, 1).entrySet().stream()
				.filter(entry -> entry.getValue() >= THRESHOLD)
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());

		if (socMappings.isEmpty()) {
			news.setState(STATE_UNCLASSIFIED);
		} else {
			socMappings.stream().findFirst().ifPresent(news::setSocCode);
			news.setState(STATE_CLASSIFIED);
		}

		newsDAO.save(List.of(news));

		String occupation = Optional.ofNullable(occupationService.getOccupation(news.getSocCode()))
				.map(Occupation::getTitle)
				.orElse(news.getSocCode());

		log.info("Classified news article {} - {}, state={}, occupation={}", news.getId(), news.getTitle(), news.getState(), occupation);
	}



}
