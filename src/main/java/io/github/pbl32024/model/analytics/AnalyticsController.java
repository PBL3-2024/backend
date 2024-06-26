package io.github.pbl32024.model.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	@GetMapping(path = "/currentOccupation", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getUserCurrentOccupationReport(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/goalOccupation", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getUserGoalOccupationReport(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/jobPostingEngagement", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getJobPostingEngagement(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/newsEngagement", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getNewsEngagement(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/certificationEngagement", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getCertificationEngagement(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/learningMaterialEngagement", produces = MediaType.APPLICATION_JSON_VALUE)
	public Report getLearningMaterialEngagement(AnalyticsQuery query) {
		return null;
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void getClickData(ClickDataQuery query, OutputStream body) {

	}

	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveClickData(@RequestBody ClickData data) {
		analyticsService.saveClickData(data);
	}

}
