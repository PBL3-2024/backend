package io.github.pbl32024.model.news;

import java.time.LocalDateTime;import io.github.pbl32024.model.ExternalLink;

public class News {

	public String id;

	public String socCode;

	public String title;

	public String source;

	public LocalDateTime published;

	public String hash;

	public String state;

	private ExternalLink externalLink;

}
