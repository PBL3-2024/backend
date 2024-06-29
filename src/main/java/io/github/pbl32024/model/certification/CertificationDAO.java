package io.github.pbl32024.model.certification;


import io.github.pbl32024.SOCSupport;
import io.github.pbl32024.model.ExternalLink;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CertificationDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Certification> getCertificationsBySocCode(String socCode) {
        CertificationRowCallbackHandler rch = new CertificationRowCallbackHandler();
        jdbcTemplate.query("""
                SELECT
                		c.id as id,
                		c.title as title,
                		c.description as description,
                		c.source as source,
                		e.url as external_link_url,
		                e.label as external_link_label,
                        e.id as external_link_id,
                		s.soc as soc
                	FROM certifications c
                	LEFT JOIN external_links e ON e.id = c.external_link_id
                	LEFT JOIN certifications_soc_codes s ON c.id = s.certification_id
	                WHERE soc LIKE CONCAT(:soc, '%')
                    ORDER BY c.id ASC;
                """, Map.of("soc", SOCSupport.trimSoc(socCode)), rch);
        return rch.getCertifications().stream().limit(10).toList();
    }

    @Transactional
    public void synchronizeCertifications() {
        CareerOneStopRowCallbackHandler rch = new CareerOneStopRowCallbackHandler();
        jdbcTemplate.query("""
                	SELECT
                		c.cert_id as id,
                		c.cert_name as title,
                		c.cert_description as description,
                		c.url as url,
                		o.org_name as source,
                		s.onetcode as soc
                	FROM careeronestop.certifications c
                	LEFT JOIN careeronestop.cert_orgs o ON c.org_id = o.org_id
                	LEFT JOIN careeronestop.cert_onet_assign s ON s.cert_id = c.cert_id;
                """, rch);
        save(rch.getCertifications().stream()
                .filter(c -> c.getExternalLink() != null)
                .map(c -> {
                    c.setSocCode(c.getSocCode().stream()
                            .map(soc -> soc.length() > 7 ? soc.substring(0, 7) : soc)
                            .collect(Collectors.toSet()));
                    c.getExternalLink().setId(UUID.randomUUID().toString());
                    return c;
                })
                .toList());
    }

    public void save(List<Certification> certifications) {
        SqlParameterSource[] certificationParameters = certifications.stream()
                .map(c -> {
                    MapSqlParameterSource source = new MapSqlParameterSource();
                    source.addValue("id", c.getId());
                    source.addValue("title", c.getTitle());
                    source.addValue("description", c.getDescription());
                    source.addValue("source", c.getSource());
                    Optional.of(c)
                            .map(Certification::getExternalLink)
                            .ifPresent(link -> source.addValue("externalLinkId", link.getId()));
                    return source;
                }).toArray(SqlParameterSource[]::new);
        SqlParameterSource[] certificationSocCodeParameters = certifications.stream()
                .flatMap(c -> {
                    List<SqlParameterSource> s = new ArrayList<>();
                    for (String socCode : c.getSocCode()) {
                        MapSqlParameterSource source = new MapSqlParameterSource();
                        source.addValue("certificationId", c.getId());
                        source.addValue("soc", socCode);
                        s.add(source);
                    }
                    return s.stream();
                }).toArray(SqlParameterSource[]::new);
        SqlParameterSource[] externalLinkParameters = certifications.stream()
                .map(Certification::getExternalLink)
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
			CREATE TEMPORARY TABLE temp_certifications AS
			SELECT * FROM certifications WHERE 1=0;
		""");

        // Populate temporary tables
        jdbcTemplate.batchUpdate("""
			INSERT INTO temp_external_links (id, url, label)
			VALUES (:id, :url, :label);		
		""", externalLinkParameters);
        jdbcTemplate.batchUpdate("""
			INSERT INTO temp_certifications (id, title, description, source, external_link_id)
			VALUES (:id, :title, :description, :source, :externalLinkId);
		""", certificationParameters);

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
			INSERT INTO certifications (id, title, description, source, external_link_id)
			SELECT id, title, description, source, external_link_id
			FROM temp_certifications
			ON CONFLICT (id) DO UPDATE
				SET title = EXCLUDED.title,
				description = EXCLUDED.description,
				source = EXCLUDED.source,
				external_link_id = EXCLUDED.external_link_id;
		""");

        // Update categories/soc codes
        jdbcTemplate.getJdbcTemplate().execute("""
			DELETE FROM certifications_soc_codes
			USING temp_certifications
			WHERE certifications_soc_codes.certification_id = temp_certifications.id;
		""");
        jdbcTemplate.batchUpdate("""
			INSERT INTO certifications_soc_codes (certification_id, soc)
			VALUES (:certificationId, :soc);
		""", certificationSocCodeParameters);

        jdbcTemplate.getJdbcTemplate().execute("DROP TABLE temp_certifications;");
        jdbcTemplate.getJdbcTemplate().execute("DROP TABLE temp_external_links;");
    }

    public class CareerOneStopRowCallbackHandler implements RowCallbackHandler {

        private Map<String, Certification> certificationMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            String id = rs.getString("id");
            Certification certification = certificationMap.computeIfAbsent(id, key -> new Certification());
            certification.setId(id);
            certification.setTitle(rs.getString("title"));
            certification.setDescription(rs.getString("description"));
            certification.setSource(rs.getString("source"));
            certification.setSocCode(new HashSet<>());

            String soc = rs.getString("soc");
            if (soc != null) {
                certification.getSocCode().add(soc);
            }

            String url = rs.getString("url");
            if (url != null) {
                ExternalLink externalLink = new ExternalLink();
                externalLink.setUrl(url);
                externalLink.setLabel("Click to learn more");
                certification.setExternalLink(externalLink);
            }
        }

        public Collection<Certification> getCertifications() {
            return certificationMap.values();
        }
    }

    public class CertificationRowCallbackHandler implements RowCallbackHandler {

        private Map<String, Certification> certificationMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            String id = rs.getString("id");
            Certification certification = certificationMap.computeIfAbsent(id, key -> new Certification());
            certification.setId(id);
            certification.setTitle(rs.getString("title"));
            certification.setDescription(rs.getString("description"));
            certification.setSource(rs.getString("source"));
            certification.setSocCode(new HashSet<>());

            String soc = rs.getString("soc");
            if (soc != null) {
                certification.getSocCode().add(soc);
            }

            String url = rs.getString("external_link_url");
            if (url != null) {
                ExternalLink externalLink = new ExternalLink();
                externalLink.setUrl(url);
                externalLink.setLabel(rs.getString("external_link_label"));
                externalLink.setId(rs.getString("external_link_id"));
                certification.setExternalLink(externalLink);
            }
        }

        public Collection<Certification> getCertifications() {
            return certificationMap.values();
        }
    }

}
