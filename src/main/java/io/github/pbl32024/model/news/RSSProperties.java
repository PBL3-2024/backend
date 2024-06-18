package io.github.pbl32024.model.news;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("backend.news.rss")
public class RSSProperties {

	private List<RSSSource> sources;

}
