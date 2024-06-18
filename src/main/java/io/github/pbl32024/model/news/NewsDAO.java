package io.github.pbl32024.model.news;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsDAO {

	public List<News> getBySocCodeAndSource(String socCode, String source, LocalDateTime before, LocalDateTime after) {
		return null;
	}

	public List<News> getBySocCode(String socCode) {
		return null;
	}

	public List<News> getByState() {
		return null;
	}

	public void save(News news) {

	}

}
