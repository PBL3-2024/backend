package io.github.pbl32024.model.analytics;

import lombok.Data;

@Data
public abstract class Chart {

	private String title;

	public abstract String getType();

}
