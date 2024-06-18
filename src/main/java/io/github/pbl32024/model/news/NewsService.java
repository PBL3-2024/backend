package io.github.pbl32024.model.news;

import io.github.pbl32024.model.occupation.OccupationService;

import java.util.List;

public class NewsService {

	private NewsDAO newsDAO;

	private OccupationService occupationService;

	private RSSClient rSSClient;

	private RSSProperties rSSProperties;

	public List<News> getNews(NewsQuery query) {
		return null;
	}

	public void fetchRSSFeeds() {

	}

	public void classifyNews() {

	}

}
