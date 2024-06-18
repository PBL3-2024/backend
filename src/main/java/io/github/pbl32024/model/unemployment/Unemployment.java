package io.github.pbl32024.model.unemployment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Unemployment {

	private String id;

	private String socCode;

	private LocalDateTime date;

	private String value;

}
