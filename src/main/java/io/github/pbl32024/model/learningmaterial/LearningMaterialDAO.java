package io.github.pbl32024.model.learningmaterial;

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

@Component
@RequiredArgsConstructor
public class LearningMaterialDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public List<LearningMaterial> getBySoc(String socCode) {
		LearningMaterialRowCallbackHandler rch = new LearningMaterialRowCallbackHandler();
		jdbcTemplate.query("""
				SELECT
				    lm.id AS learning_material_id,
				    lm.title,
				    lm.description,
				    lm.source,
				    lm.type,
				    el.id AS external_link_id,
				    el.url,
				    el.label,
				    lmsc.soc
				FROM
				    learning_material lm
				LEFT JOIN
				    learning_material_soc_codes lmsc ON lm.id = lmsc.learning_material_id
				LEFT JOIN
				    learning_material_external_links lmel ON lm.id = lmel.learning_material_id
				LEFT JOIN
				    external_links el ON lmel.external_link_id = el.id
				WHERE
				    lmsc.soc LIKE CONCAT(:soc, '%')
				ORDER BY lm.id ASC;
				""", Map.of("soc", SOCSupport.trimSoc(socCode)), rch);
		return new ArrayList<>(rch.getLearningMaterials());
	}

	@Transactional
	public void save(LearningMaterial learningMaterial) {
		MapSqlParameterSource learningMaterialParams = new MapSqlParameterSource();
		learningMaterialParams.addValue("id", learningMaterial.getId());
		learningMaterialParams.addValue("title", learningMaterial.getTitle());
		learningMaterialParams.addValue("description", learningMaterial.getDescription());
		learningMaterialParams.addValue("source", learningMaterial.getSource());
		learningMaterialParams.addValue("type", learningMaterial.getType().name());
		SqlParameterSource[] externalLinkParams = learningMaterial.getExternalLink().stream()
						.map(link -> {
							MapSqlParameterSource source = new MapSqlParameterSource();
							source.addValue("id", link.getId());
							source.addValue("url", link.getUrl());
							source.addValue("label", link.getLabel());
							source.addValue("learning_material_id", learningMaterial.getId());
							return source;
						})
						.toArray(SqlParameterSource[]::new);
		SqlParameterSource[] socParams = learningMaterial.getSocCode().stream()
						.map(soc -> {
							MapSqlParameterSource source = new MapSqlParameterSource();
							source.addValue("learning_material_id", learningMaterial.getId());
							source.addValue("soc", soc);
							return source;
						})
						.toArray(SqlParameterSource[]::new);


		jdbcTemplate.update("""
				INSERT INTO learning_material (id, title, description, source, type)
				VALUES (:id, :title, :description, :source, :type)
				ON CONFLICT (id) DO UPDATE
				SET title = :title,
				description = :description,
				source = :source,
				type = :type;
				""", learningMaterialParams);

		// Clean up tables
		jdbcTemplate.update("""
			DELETE FROM learning_material_external_links
			WHERE learning_material_id = :id;
		""", Map.of("id", learningMaterial.getId()));
		jdbcTemplate.update("""
			DELETE FROM learning_material_soc_codes
			WHERE learning_material_id = :id;
		""", Map.of("id", learningMaterial.getId()));

		// Handle soc codes
		jdbcTemplate.batchUpdate("""
				INSERT INTO learning_material_soc_codes (learning_material_id, soc)
				VALUES (:learning_material_id, :soc);
				""", socParams);

		// Handle external links
		jdbcTemplate.batchUpdate("""
			INSERT INTO external_links (id, url, label)
			VALUES (:id, :url, :label)
			ON CONFLICT (id) DO UPDATE
			SET url = :url,
			label = :label;
			""", externalLinkParams);
		jdbcTemplate.batchUpdate("""
			INSERT INTO learning_material_external_links (learning_material_id, external_link_id)
			VALUES (:learning_material_id, :id);
			""", externalLinkParams);
	}

	@Transactional
	public void deleteById(String id) {
		jdbcTemplate.update("""
			DELETE FROM learning_material_external_links
			WHERE learning_material_id = :id;
		""", Map.of("id", id));
		jdbcTemplate.update("""
			DELETE FROM learning_material_soc_codes
			WHERE learning_material_id = :id;
		""", Map.of("id", id));
		jdbcTemplate.update("""
			DELETE FROM learning_material
			WHERE id = :id;
		""", Map.of("id", id));
	}

	private static class LearningMaterialRowCallbackHandler implements RowCallbackHandler {
		private final Map<String, LearningMaterial> learningMaterialMap = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			String learningMaterialId = rs.getString("learning_material_id");
			LearningMaterial learningMaterial = learningMaterialMap.computeIfAbsent(learningMaterialId, id -> new LearningMaterial());
			learningMaterial.setId(learningMaterialId);
			learningMaterial.setTitle(rs.getString("title"));
			learningMaterial.setDescription(rs.getString("description"));
			learningMaterial.setSource(rs.getString("source"));
			learningMaterial.setType(LearningContentType.valueOf(rs.getString("type")));
			learningMaterial.setSocCode(new HashSet<>());
			learningMaterial.setExternalLink(new HashSet<>());

			String socCode = rs.getString("soc");
			if (socCode != null) {
				learningMaterial.getSocCode().add(socCode);
			}

			String externalLinkId = rs.getString("external_link_id");
			if (externalLinkId != null) {
				ExternalLink externalLink = new ExternalLink();
				externalLink.setId(externalLinkId);
				externalLink.setUrl(rs.getString("url"));
				externalLink.setLabel(rs.getString("label"));
				learningMaterial.getExternalLink().add(externalLink);
			}
		}

		public Collection<LearningMaterial> getLearningMaterials() {
			return learningMaterialMap.values();
		}
	}

}
