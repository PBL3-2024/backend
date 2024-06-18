package io.github.pbl32024.model.news;

import io.github.pbl32024.model.occupation.OccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsDAO newsDAO;

	private final OccupationService occupationService;

	private final RSSClient rSSClient;

	private final RSSProperties rSSProperties;

	public List<News> getNews(NewsQuery query) {
		return null;
	}

	public void fetchRSSFeeds() {

	}

	public void classifyNews() {

	}

}
