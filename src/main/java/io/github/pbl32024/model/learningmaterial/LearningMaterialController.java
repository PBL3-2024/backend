package io.github.pbl32024.model.learningmaterial;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/learning")
public class LearningMaterialController {

	private final LearningMaterialService learningMaterialService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public LearningMaterialResponse getLearningMaterial(LearningMaterialQuery query) {
		return null;
	}

}
