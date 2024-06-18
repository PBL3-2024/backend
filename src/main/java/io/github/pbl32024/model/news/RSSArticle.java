package io.github.pbl32024.model.news;


import lombok.Data;

import java.util.List;

@Data
public class RSSArticle {

	private String title;

	private String description;

	private String link;

	private String pubDate;

	private List<String> category;

}
