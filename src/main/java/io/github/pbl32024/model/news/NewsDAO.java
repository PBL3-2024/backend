package io.github.pbl32024.model.news;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NewsDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final List<News> newNews = Collections.synchronizedList(new ArrayList<>());
	private final Map<String, List<News>> socNews = new ConcurrentHashMap<>();

	public List<News> getBySocCodeAndSource(String socCode, String source, LocalDateTime before, LocalDateTime after) {
		return null;
	}

	public List<News> getBySocCode(String socCode) {
		return socNews.getOrDefault(socCode, List.of());
	}

	public News getNewNews() {
		return newNews.isEmpty() ? null : newNews.removeFirst();
	}

	public void save(List<News> news) {
		// Set up parameter sources
		SqlParameterSource[] newsParameters = news.stream()
				.map(n -> {
					MapSqlParameterSource source = new MapSqlParameterSource();
					source.addValue("id", n.getId());
					source.addValue("title", n.getTitle());
					source.addValue("description", n.getDescription());
					source.addValue("published", n.getPublished());
					source.addValue("source", n.getSource());
					source.addValue("state", n.getSource());
					Optional.of(n)
							.map(News::getExternalLink)
							.ifPresent(link -> source.addValue("externalLinkId", link.getId()));
					return source;
				}).toArray(SqlParameterSource[]::new);
		SqlParameterSource[] newsSocCodeParameters = news.stream()
				.flatMap(n -> {
					List<SqlParameterSource> s = new ArrayList<>();
					for (String socCode : n.getSocCode()) {
						MapSqlParameterSource source = new MapSqlParameterSource();
						source.addValue("newsId", n.getId());
						source.addValue("soc", socCode);
						s.add(source);
					}
					return s.stream();
				}).toArray(SqlParameterSource[]::new);
		SqlParameterSource[] newsCategoryParameters = news.stream()
				.flatMap(n -> {
					List<SqlParameterSource> s = new ArrayList<>();
					for (String socCode : n.getSocCode()) {
						MapSqlParameterSource source = new MapSqlParameterSource();
						source.addValue("newsId", n.getId());
						source.addValue("category", socCode);
						s.add(source);
					}
					return s.stream();
				}).toArray(SqlParameterSource[]::new);

		// Set up temporary tables for merges
		jdbcTemplate.getJdbcTemplate().update("""
			CREATE TEMPORARY TABLE temp_news AS
			SELECT * FROM news WHERE 1=0;
		""");
		jdbcTemplate.getJdbcTemplate().update("""
			CREATE TEMPORARY TABLE temp_news_soc_codes AS
			SELECT * FROM news_soc_codes WHERE 1=0;
		""");
		jdbcTemplate.getJdbcTemplate().update("""
			CREATE TEMPORARY TABLE temp_news_categories AS
			SELECT * FROM news_categories WHERE 1=0;
		""");

		// Populate temporary tables
		jdbcTemplate.batchUpdate("""
			INSERT INTO temp_news (id, title, description, published, source, state, external_link_id)
			VALUES (:id, :title, :description, :published, :source, :state, :external_link_id);
		""", newsParameters);
		jdbcTemplate.batchUpdate("""
			INSERT INTO temp_news_soc_codes (news_id, soc)
			VALUES (:newsId, :soc);
		""", newsSocCodeParameters);
		jdbcTemplate.batchUpdate("""
			INSERT INTO temp_news_categoriers (news_id, category)
			VALUES (:newsId, :category);
		""", newsCategoryParameters);

		jdbcClient.sql("""
			MERGE INTO news AS target
			USING (VALUES
				(1, 'New Title', 'New Content', '2024-06-27')
			) AS source (id, title, content, published_date)
			ON target.id = source.id
			WHEN MATCHED THEN
				UPDATE SET
				title = source.title,
				content = source.content,
				published_date = source.published_date
			WHEN NOT MATCHED THEN
				INSERT (id, title, content, published_date)
				VALUES (source.id, source.title, source.content, source.published_date);
		""");
		for (News n : news) {
			if ("NEW".equals(n.getState())) {
				newNews.add(n);
			} else if (n.getSocCode() != null) {
				socNews.computeIfAbsent(n.getSocCode(), k -> Collections.synchronizedList(new ArrayList<>())).add(n);
			}
		}
	}

}
