package io.github.pbl32024.model.contentful;

import io.github.pbl32024.model.learningmaterial.LearningMaterial;
import io.github.pbl32024.model.learningmaterial.LearningMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentfulService {

	private static final String LOCALE = "en-US";

	private final LearningMaterialService learningMaterialService;

	public void putEntry(ContentfulPublishedRequest entry) {
		LearningMaterial learningMaterial = new LearningMaterial();
		learningMaterial.setId(entry.getSys().getId());
		learningMaterial.setTitle(entry.getFields().getTitle().get(LOCALE));
		learningMaterial.setSocCode(entry.getFields().getSocCodes().get(LOCALE));
		learningMaterial.setExternalLink(entry.getFields().getExternalLinks().get(LOCALE));
		learningMaterial.setDescription(entry.getFields().getDescription().get(LOCALE));
		learningMaterial.setType(entry.getFields().getType().get(LOCALE));
		learningMaterial.setSource("Contentful");
		learningMaterialService.updateLearningMaterial(learningMaterial);
	}

	public void removeEntry(ContentfulUnpublishedRequest entry) {
		LearningMaterial learningMaterial = new LearningMaterial();
		learningMaterial.setId(entry.getSys().getId());
		learningMaterialService.deleteLearningMaterial(learningMaterial);
	}

}
