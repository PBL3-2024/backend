package io.github.pbl32024.model.news;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RSSClient {

	private final RSSWebClient rssWebClient;

	public RSSFeed fetchRssFeed(RSSSource source) {
		RSSFeed feed = rssWebClient.getRSSFeed(source.getUrl());
		feed.setSource(source);
		return feed;
	}

}
