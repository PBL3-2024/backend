package io.github.pbl32024.model.learningmaterial;

import io.github.pbl32024.model.ExternalLink;

public class LearningMaterial {

	public String id;

	public String title;

	public String description;

	public String[] socCode;

	public String organization;

	private LearningContentType learningContentType;

	private ExternalLink[] externalLink;

}
