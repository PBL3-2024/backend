package io.github.pbl32024.model.contentful;

import io.github.pbl32024.model.contentful.contentfulpublishedrequest.ContentfulPublishedRequest;
import io.github.pbl32024.model.contentful.contentfulunpublishedrequest.ContentfulUnpublishedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contentful")
public class ContentfulController {

	private final ContentfulService contentfulService;

	@PostMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void notifyEntryPublished(@RequestBody ContentfulPublishedRequest entry) {

	}

	@PostMapping(path = "/unpublish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void notifyEntryUnpublished(@RequestBody ContentfulUnpublishedRequest entry) {

	}

}
