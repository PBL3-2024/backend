package io.github.pbl32024.model.learningmaterial;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LearningMaterialDAO {

	public List<LearningMaterial> getBySoc(String socCode) {
		return null;
	}

	public List<LearningMaterial> getBySocAndContentType(String socCode, LearningContentType type) {
		return null;
	}

	public void save(LearningMaterial learningMaterial) {

	}

	public void deleteById(String id) {

	}

}
