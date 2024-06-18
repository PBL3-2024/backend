package io.github.pbl32024.model.analytics;

import lombok.Data;

@Data
public class ClickData {

	private long timestamp;

	private String elementId;

	private String elementType;

	private String userId;

	private String userPostalCode;

	private String userCurrentOccupation;

	private String userGoalOccupation;

}
