package io.github.pbl32024.model.contentful;

import io.github.pbl32024.model.learningmaterial.LearningMaterialService;
import io.github.pbl32024.model.contentful.contentfulpublishedrequest.ContentfulPublishedRequest;
import io.github.pbl32024.model.contentful.contentfulunpublishedrequest.ContentfulUnpublishedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentfulService {

	private final LearningMaterialService learningMaterialService;

	public void putEntry(ContentfulPublishedRequest entry) {

	}

	public void removeEntry(ContentfulUnpublishedRequest entry) {

	}

}
