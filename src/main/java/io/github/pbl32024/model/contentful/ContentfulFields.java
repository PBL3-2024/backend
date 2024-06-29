package io.github.pbl32024.model.contentful;

import io.github.pbl32024.model.ExternalLink;
import io.github.pbl32024.model.learningmaterial.LearningContentType;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ContentfulFields {

	private Map<String, String> title;

	private Map<String, Set<String>> socCodes;

	private Map<String, Set<ExternalLink>> externalLinks;

	private Map<String, String> description;

	private Map<String, LearningContentType> type;

}
