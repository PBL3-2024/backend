package io.github.pbl32024.model.news;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NewsDAO {

	private final List<News> newNews = Collections.synchronizedList(new ArrayList<>());
	private final Map<String, List<News>> socNews = new ConcurrentHashMap<>();

	public List<News> getBySocCodeAndSource(String socCode, String source, LocalDateTime before, LocalDateTime after) {
		return null;
	}

	public List<News> getBySocCode(String socCode) {
		return socNews.getOrDefault(socCode, List.of());
	}

	public News getNewNews() {
		return newNews.isEmpty() ? null : newNews.removeFirst();
	}

	public void save(List<News> news) {
		for (News n : news) {
			if ("NEW".equals(n.getState())) {
				newNews.add(n);
			} else if (n.getSocCode() != null) {
				socNews.computeIfAbsent(n.getSocCode(), k -> Collections.synchronizedList(new ArrayList<>())).add(n);
			}
		}
	}

}
