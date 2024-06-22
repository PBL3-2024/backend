package io.github.pbl32024.model.news;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final NewsService newsService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public NewsResponse getNews(NewsQuery query) {
		NewsResponse newsResponse = new NewsResponse();
		newsResponse.setNews(newsService.getNews(query));
		return newsResponse;
	}

}
