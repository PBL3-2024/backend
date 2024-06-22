package io.github.pbl32024.model.news;

import java.time.LocalDateTime;
import java.util.List;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

@Data
public class News {

	private String id;

	private String socCode;

	private String title;

	private String description;

	private List<String> categories;

	private String source;

	private LocalDateTime published;

	private String hash;

	private String state;

	private ExternalLink externalLink;

}
