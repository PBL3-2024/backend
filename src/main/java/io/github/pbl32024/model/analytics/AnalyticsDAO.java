package io.github.pbl32024.model.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticsDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public Chart getUserCurrentOccupationPieChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getUserCurrentOccupationMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getUserGoalOccupationPieChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getUserGoalOccupationMapChart(AnalyticsQuery query) {
		return null;
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
		return null;
	}

	public Chart getCertificationEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getCertificationEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getCertificationEngagementMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getLearningMaterialEngagementCurrentOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getLearningMaterialEngagementGoalOccupationHeatMapChart(AnalyticsQuery query) {
		return null;
	}

	public Chart getLearningMaterialEngagementMapChart(AnalyticsQuery query) {
		return null;
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

}
