package io.github.pbl32024.model.news;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsQuery {

	private String socCode;

	private String source;

	private LocalDateTime after;

	private LocalDateTime before;

}
