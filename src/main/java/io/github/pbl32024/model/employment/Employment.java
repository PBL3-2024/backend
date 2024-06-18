package io.github.pbl32024.model.employment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Employment {

	private String id;

	private String socCode;

	private LocalDateTime date;

	private long value;

	private boolean forecasted;

}
