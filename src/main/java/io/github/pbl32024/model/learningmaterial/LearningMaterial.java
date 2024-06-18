package io.github.pbl32024.model.learningmaterial;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

import java.util.List;

@Data
public class LearningMaterial {

	private String id;

	private String title;

	private String description;

	private List<String> socCode;

	private String organization;

	private LearningContentType learningContentType;

	private List<ExternalLink> externalLink;

}
