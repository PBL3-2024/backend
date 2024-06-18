package io.github.pbl32024.model.news;

import java.time.LocalDateTime;import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

@Data
public class News {

	private String id;

	private String socCode;

	private String title;

	private String source;

	private LocalDateTime published;

	private String hash;

	private String state;

	private ExternalLink externalLink;

}
