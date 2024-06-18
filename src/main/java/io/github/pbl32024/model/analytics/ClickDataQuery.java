package io.github.pbl32024.model.analytics;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClickDataQuery {

	private LocalDateTime start;

	private LocalDateTime end;

	private String userPostalCode;

	private String userCurrentOccupation;

	private String userGoalOccupation;

	private String elementType;

}
