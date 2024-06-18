package io.github.pbl32024.model.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

	private final AnalyticsDAO analyticsDAO;

	public Report getUserCurrentOccupationReport(AnalyticsQuery query) {
		return null;
	}

	public Report getUserGoalOccupationReport(AnalyticsQuery query) {
		return null;
	}

	public Report getJobPostingEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getNewsEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getCertificationEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getLearningMaterialEngagement(AnalyticsQuery query) {
		return null;
	}

	public void getClickData(ClickDataQuery query, OutputStream body) {

	}

	public void saveClickData(ClickData query) {

	}

}
