package io.github.pbl32024.model.learningmaterial;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningMaterialService {

	private final LearningMaterialDAO learningMaterialDAO;

	public List<LearningMaterial> getLearningMaterial(LearningMaterialQuery query) {
		return learningMaterialDAO.getBySoc(query.getSocCode());
	}

	public void updateLearningMaterial(LearningMaterial learningMaterial) {
		learningMaterialDAO.save(learningMaterial);
	}

	public void deleteLearningMaterial(LearningMaterial learningMaterial) {
		learningMaterialDAO.deleteById(learningMaterial.getId());
	}

}
