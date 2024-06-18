package io.github.pbl32024.model.analytics;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsQuery {

	private LocalDateTime start;

	private LocalDateTime end;

	private String socCode;

}
