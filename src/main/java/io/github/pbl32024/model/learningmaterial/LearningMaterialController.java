package io.github.pbl32024.model.learningmaterial;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

public class LearningMaterialController {

	private LearningMaterialService learningMaterialService;

	public LearningMaterialResponse getLearningMaterial(LearningMaterialQuery query, SpringDataWebProperties.Sort sort) {
		return null;
	}

}
