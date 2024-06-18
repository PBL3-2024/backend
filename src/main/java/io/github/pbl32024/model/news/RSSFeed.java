package io.github.pbl32024.model.news;

import lombok.Data;

import java.util.List;

@Data
public class RSSFeed {

	private List<RSSArticle> item;

	private RSSSource source;

}
