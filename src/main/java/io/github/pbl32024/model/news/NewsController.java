package io.github.pbl32024.model.news;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

public class NewsController {

	private NewsService newsService;

	public NewsResponse getNews(NewsQuery query, SpringDataWebProperties.Sort sort) {
		return null;
	}

}
