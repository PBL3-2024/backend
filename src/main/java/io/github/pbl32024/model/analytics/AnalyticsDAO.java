package io.github.pbl32024.model.analytics;

import io.github.pbl32024.SOCSupport;
import io.github.pbl32024.model.occupation.Occupation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AnalyticsDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final Resource postalCodes;

	private final Map<String, String> postalCodeLookup = new ConcurrentHashMap<>();

	public AnalyticsDAO(NamedParameterJdbcTemplate jdbcTemplate, @Value("${backend.analytics.postal-codes}") Resource postalCodes) {
		this.jdbcTemplate = jdbcTemplate;
		this.postalCodes = postalCodes;
	}

	@PostConstruct
	public void loadOccupationTrie() throws Exception {
		try (CSVParser parser = CSVFormat.EXCEL.builder()
				.setHeader()
				.setSkipHeaderRecord(true)
				.build().parse(new InputStreamReader(postalCodes.getInputStream()))) {
			for (CSVRecord record : parser) {
				postalCodeLookup.put(record.get("zip"), record.get("state_id"));
			}
		}
	}

	public Chart getUserCurrentOccupationPieChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getUserCurrentOccupationMapChart(AnalyticsQuery query) {
		try (Stream<Tuple> data = jdbcTemplate.queryForStream("""
				SELECT
				    postal_code,
				    COUNT(*) AS user_count
				FROM
				    public.profile
				WHERE
				    current_soc LIKE CONCAT(:prefix, '%')
					AND postal_code IS NOT NULL
				GROUP BY
				    postal_code;
				""", Map.of("prefix", SOCSupport.trimSoc(query.getSocCode())), (rs, i) -> {
			Tuple tuple = new Tuple();
			tuple.setKey(rs.getString("postal_code"));
			tuple.setValue(rs.getInt("user_count"));
			return tuple;
		})) {
			Map<String, Optional<Tuple>> statewise = data.filter(d -> postalCodeLookup.containsKey(d.getKey())).collect(Collectors.groupingBy(
					d -> postalCodeLookup.get(d.getKey()),
					Collectors.reducing(this::combine)));

			List<Tuple> combined = new ArrayList<>();
			statewise.forEach((state, d) -> {
				d.ifPresent(o -> {
					o.setKey(state);
					combined.add(o);
				});
			});

			MapChart mapChart = new MapChart();
			mapChart.setTitle("Users by State");
			mapChart.setType("map");
			mapChart.setData(combined);
			return mapChart;
		}
	}

	public Chart getUserGoalOccupationPieChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getUserGoalOccupationMapChart(AnalyticsQuery query) {
		try (Stream<Tuple> data = jdbcTemplate.queryForStream("""
				SELECT
				    postal_code,
				    COUNT(*) AS user_count
				FROM
				    public.profile
				WHERE
				    goal_soc LIKE CONCAT(:prefix, '%')
					AND postal_code IS NOT NULL
				GROUP BY
				    postal_code;
				""", Map.of("prefix", SOCSupport.trimSoc(query.getSocCode())), (rs, i) -> {
			Tuple tuple = new Tuple();
			tuple.setKey(rs.getString("postal_code"));
			tuple.setValue(rs.getInt("user_count"));
			return tuple;
		})) {
			Map<String, Optional<Tuple>> statewise = data.filter(d -> postalCodeLookup.containsKey(d.getKey())).collect(Collectors.groupingBy(
					d -> postalCodeLookup.get(d.getKey()),
					Collectors.reducing(this::combine)));

			List<Tuple> combined = new ArrayList<>();
			statewise.forEach((state, d) -> {
				d.ifPresent(o -> {
					o.setKey(state);
					combined.add(o);
				});
			});

			MapChart mapChart = new MapChart();
			mapChart.setTitle("Users by State");
			mapChart.setType("map");
			mapChart.setData(combined);
			return mapChart;
		}
	}

	public Chart getUserGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getJobPostingEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getJobPostingEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getJobPostingEngagementMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getNewsEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getNewsEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getNewsEngagementMapChart(AnalyticsQuery query) {
		try (Stream<Tuple> data = jdbcTemplate.queryForStream("""
				SELECT
					cd.user_postal_code as postal_code,
					COUNT(*) AS total_clicks
				FROM (
					SELECT DISTINCT ON (c.timestamp, c.element_id, c.user_id)
						c.user_postal_code, c.timestamp, c.element_id, c.user_id, csc.soc
					FROM click_data c
					INNER JOIN news_soc_codes csc on c.element_id = csc.news_id
					WHERE c.element_type = 'NEWS'
					AND csc.soc LIKE CONCAT(:prefix, '%')
				) cd
				WHERE cd.user_postal_code IS NOT NULL
				GROUP BY
					cd.user_postal_code;
				""", Map.of("prefix", SOCSupport.trimSoc(query.getSocCode())), (rs, i) -> {
			Tuple tuple = new Tuple();
			tuple.setKey(rs.getString("postal_code"));
			tuple.setValue(rs.getInt("total_clicks"));
			return tuple;
		})) {
			Map<String, Optional<Tuple>> statewise = data.filter(d -> postalCodeLookup.containsKey(d.getKey())).collect(Collectors.groupingBy(
					d -> postalCodeLookup.get(d.getKey()),
					Collectors.reducing(this::combine)));

			List<Tuple> combined = new ArrayList<>();
			statewise.forEach((state, d) -> {
				d.ifPresent(o -> {
					o.setKey(state);
					combined.add(o);
				});
			});

			MapChart mapChart = new MapChart();
			mapChart.setTitle("Users by State");
			mapChart.setType("map");
			mapChart.setData(combined);
			return mapChart;
		}
	}

	public Chart getCertificationEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getCertificationEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getCertificationEngagementMapChart(AnalyticsQuery query) {
		try (Stream<Tuple> data = jdbcTemplate.queryForStream("""
				SELECT
					cd.user_postal_code as postal_code,
					COUNT(*) AS total_clicks
				FROM (
					SELECT DISTINCT ON (c.timestamp, c.element_id, c.user_id)
						c.user_postal_code, c.timestamp, c.element_id, c.user_id, csc.soc
					FROM click_data c
					INNER JOIN certifications_soc_codes csc on c.element_id = csc.certification_id
					WHERE c.element_type = 'CERTIFICATIONS'
					AND csc.soc LIKE CONCAT(:prefix, '%')
				) cd
				WHERE cd.user_postal_code IS NOT NULL
				GROUP BY
					cd.user_postal_code;
				""", Map.of("prefix", SOCSupport.trimSoc(query.getSocCode())), (rs, i) -> {
			Tuple tuple = new Tuple();
			tuple.setKey(rs.getString("postal_code"));
			tuple.setValue(rs.getInt("total_clicks"));
			return tuple;
		})) {
			Map<String, Optional<Tuple>> statewise = data.filter(d -> postalCodeLookup.containsKey(d.getKey())).collect(Collectors.groupingBy(
					d -> postalCodeLookup.get(d.getKey()),
					Collectors.reducing(this::combine)));

			List<Tuple> combined = new ArrayList<>();
			statewise.forEach((state, d) -> {
				d.ifPresent(o -> {
					o.setKey(state);
					combined.add(o);
				});
			});

			MapChart mapChart = new MapChart();
			mapChart.setTitle("Users by State");
			mapChart.setType("map");
			mapChart.setData(combined);
			return mapChart;
		}
	}

	public Chart getLearningMaterialEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getLearningMaterialEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getLearningMaterialEngagementMapChart(AnalyticsQuery query) {
		try (Stream<Tuple> data = jdbcTemplate.queryForStream("""
				SELECT
					cd.user_postal_code as postal_code,
					COUNT(*) AS total_clicks
				FROM (
					SELECT DISTINCT ON (c.timestamp, c.element_id, c.user_id)
						c.user_postal_code, c.timestamp, c.element_id, c.user_id, csc.soc
					FROM click_data c
					INNER JOIN learning_material_soc_codes csc on c.element_id = csc.learning_material_id
					WHERE c.element_type = 'LEARNING_MATERIAL'
					AND csc.soc LIKE CONCAT(:prefix, '%')
				) cd
				WHERE cd.user_postal_code IS NOT NULL
				GROUP BY
					cd.user_postal_code;
				""", Map.of("prefix", SOCSupport.trimSoc(query.getSocCode())), (rs, i) -> {
			Tuple tuple = new Tuple();
			tuple.setKey(rs.getString("postal_code"));
			tuple.setValue(rs.getInt("total_clicks"));
			return tuple;
		})) {
			Map<String, Optional<Tuple>> statewise = data.filter(d -> postalCodeLookup.containsKey(d.getKey())).collect(Collectors.groupingBy(
					d -> postalCodeLookup.get(d.getKey()),
					Collectors.reducing(this::combine)));

			List<Tuple> combined = new ArrayList<>();
			statewise.forEach((state, d) -> {
				d.ifPresent(o -> {
					o.setKey(state);
					combined.add(o);
				});
			});

			MapChart mapChart = new MapChart();
			mapChart.setTitle("Users by State");
			mapChart.setType("map");
			mapChart.setData(combined);
			return mapChart;
		}
	}

	public int getClickData(ClickDataQuery query) {
		return 0;
	}

	public void saveClickData(List<ClickData> data) {
		SqlParameterSource[] params = data.stream()
				.map(d -> {
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("timestamp", Instant.ofEpochMilli(d.getTimestamp()).atOffset(ZoneOffset.UTC));
					param.addValue("elementId", d.getElementId());
					param.addValue("elementType", d.getElementType().name());
					param.addValue("userId", d.getUserId());
					param.addValue("userPostalCode", d.getUserPostalCode());
					param.addValue("userCurrentSoc", d.getUserCurrentOccupation());
					param.addValue("userGoalSoc", d.getUserGoalOccupation());
					return param;
				})
				.toArray(SqlParameterSource[]::new);
		jdbcTemplate.batchUpdate("""
				INSERT INTO click_data (timestamp, element_id, element_type, user_id, user_postal_code, user_current_soc, user_goal_soc)
				VALUES (:timestamp, :elementId, :elementType, :userId, :userPostalCode, :userCurrentSoc, :userGoalSoc); 
				""", params);
	}

	private Tuple combine(Tuple a, Tuple b) {
		Tuple combined = new Tuple();
		combined.setKey(a.getKey());
		combined.setValue(a.getValue() + b.getValue());
		return combined;
	}

}
