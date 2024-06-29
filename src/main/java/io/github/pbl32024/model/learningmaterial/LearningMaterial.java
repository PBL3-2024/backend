package io.github.pbl32024.model.learningmaterial;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LearningMaterial {

	private String id;

	private String title;

	private String description;

	private Set<String> socCode;

	private String source;

	private LearningContentType type;

	private Set<ExternalLink> externalLink;

}
