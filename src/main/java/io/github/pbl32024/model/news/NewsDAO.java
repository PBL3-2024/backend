package io.github.pbl32024.model.news;

import io.github.pbl32024.SOCSupport;
import io.github.pbl32024.model.ExternalLink;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NewsDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final List<News> newNews = Collections.synchronizedList(new ArrayList<>());
	private final Map<String, List<News>> socNews = new ConcurrentHashMap<>();

	@Transactional(readOnly = true)
	public List<News> getBySocCodeAndSource(String socCode, String source, LocalDateTime before, LocalDateTime after) {
		return null;
	}

	@Transactional(readOnly = true)
	public List<News> getBySocCode(String socCode) {
		NewsRowCallbackHandler rch = new NewsRowCallbackHandler();
		jdbcTemplate.query("""
                     SELECT n.id, n.title, n.description, n.source, n.published, n.state, n.external_link_id, 
                            el.label AS external_link_label, el.url AS external_link_url, 
                            ns.soc, nc.category 
                     FROM news n 
                     LEFT JOIN external_links el ON n.external_link_id = el.id 
                     LEFT JOIN news_soc_codes ns ON n.id = ns.news_id 
                     LEFT JOIN news_categories nc ON n.id = nc.news_id 
                     WHERE ns.soc LIKE CONCAT(:soc, '%')
					 ORDER BY n.published DESC;
                     """, Map.of("soc", SOCSupport.trimSoc(socCode)), rch);
		return new ArrayList<>(rch.getNews());
	}

	@Transactional(readOnly = true)
	public News getNewNews() {
		NewsRowCallbackHandler rch = new NewsRowCallbackHandler();
		jdbcTemplate.query("""
                     WITH single_news AS (
    					SELECT * FROM news WHERE news.state = :state LIMIT 1
					)					
                     SELECT n.id, n.title, n.description, n.source, n.published, n.state, n.external_link_id, 
                            el.label AS external_link_label, el.url AS external_link_url, 
                            ns.soc, nc.category 
                     FROM single_news n 
                     LEFT JOIN external_links el ON n.external_link_id = el.id 
                     LEFT JOIN news_soc_codes ns ON n.id = ns.news_id 
                     LEFT JOIN news_categories nc ON n.id = nc.news_id;
                     """, Map.of("state", "NEW"), rch);
		return rch.getNews().stream().findFirst().orElse(null);
	}

	@Transactional
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
					source.addValue("state", n.getState());
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
					for (String socCode : n.getCategories()) {
						MapSqlParameterSource source = new MapSqlParameterSource();
						source.addValue("newsId", n.getId());
						source.addValue("category", socCode);
						s.add(source);
					}
					return s.stream();
				}).toArray(SqlParameterSource[]::new);
		SqlParameterSource[] externalLinkParameters = news.stream()
				.map(News::getExternalLink)
				.filter(Objects::nonNull)
				.map(link -> {
					MapSqlParameterSource source = new MapSqlParameterSource();
					source.addValue("id", link.getId());
					source.addValue("url", link.getUrl());
					source.addValue("label", link.getLabel());
					return source;
				})
				.toArray(SqlParameterSource[]::new);

		// Set up temporary tables for merges
		jdbcTemplate.getJdbcTemplate().update("""
			CREATE TEMPORARY TABLE temp_external_links AS
			SELECT * FROM external_links WHERE 1=0;
		""");
		jdbcTemplate.getJdbcTemplate().update("""
			CREATE TEMPORARY TABLE temp_news AS
			SELECT * FROM news WHERE 1=0;
		""");

		// Populate temporary tables
		jdbcTemplate.batchUpdate("""
			INSERT INTO temp_external_links (id, url, label)
			VALUES (:id, :url, :label);		
		""", externalLinkParameters);
		jdbcTemplate.batchUpdate("""
			INSERT INTO temp_news (id, title, description, published, source, state, external_link_id)
			VALUES (:id, :title, :description, :published, :source, :state, :externalLinkId);
		""", newsParameters);

		// Merge into main tables
		jdbcTemplate.getJdbcTemplate().execute("""
			INSERT INTO external_links (id, url, label)
			SELECT id, url, label
			FROM temp_external_links
			ON CONFLICT (id) DO UPDATE
				SET url = EXCLUDED.url,
				label = EXCLUDED.label;
		""");
		jdbcTemplate.getJdbcTemplate().execute("""
			INSERT INTO news (id, title, description, published, source, state, external_link_id)
			SELECT id, title, description, published, source, state, external_link_id
			FROM temp_news
			ON CONFLICT (id) DO UPDATE
				SET title = EXCLUDED.title,
				description = EXCLUDED.description,
				published = EXCLUDED.published,
				source = EXCLUDED.source,
				state = EXCLUDED.state,
				external_link_id = EXCLUDED.external_link_id;
		""");

		// Update categories/soc codes
		jdbcTemplate.getJdbcTemplate().execute("""
			DELETE FROM news_soc_codes
			USING temp_news
			WHERE news_soc_codes.news_id = temp_news.id;
		""");
		jdbcTemplate.getJdbcTemplate().execute("""
   			DELETE FROM news_categories
   			USING temp_news
   			WHERE news_categories.news_id = temp_news.id;
		""");
		jdbcTemplate.batchUpdate("""
			INSERT INTO news_soc_codes (news_id, soc)
			VALUES (:newsId, :soc);
		""", newsSocCodeParameters);
		jdbcTemplate.batchUpdate("""
			INSERT INTO news_categories (news_id, category)
			VALUES (:newsId, :category);
		""", newsCategoryParameters);

		jdbcTemplate.getJdbcTemplate().execute("DROP TABLE temp_news;");
		jdbcTemplate.getJdbcTemplate().execute("DROP TABLE temp_external_links;");
	}

	private static final class NewsRowCallbackHandler implements RowCallbackHandler {
		// using LinkedHashMap to maintain insertion order
		private Map<String, News> newsMap = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			String id = rs.getString("id");

			News news = newsMap.computeIfAbsent(id, i -> new News());

			if (news.getId() == null) {
				news.setId(rs.getString("id"));
				news.setTitle(rs.getString("title"));
				news.setDescription(rs.getString("description"));
				news.setSource(rs.getString("source"));
				news.setPublished(rs.getObject("published", OffsetDateTime.class).toLocalDateTime());
				news.setState(rs.getString("state"));
				news.setSocCode(new HashSet<>());
				news.setCategories(new HashSet<>());

				ExternalLink externalLink = new ExternalLink();
				externalLink.setId(rs.getString("external_link_id"));
				externalLink.setLabel(rs.getString("external_link_label"));
				externalLink.setUrl(rs.getString("external_link_url"));

				news.setExternalLink(externalLink);
			}

			// Collect soc codes and categories
			String soc = rs.getString("soc");
			if (soc != null) {
				news.getSocCode().add(soc);
			}
			String category = rs.getString("category");
			if (category != null) {
				news.getCategories().add(category);
			}
		}

		public Collection<News> getNews() {
			return newsMap.values();
		}
	}

}
