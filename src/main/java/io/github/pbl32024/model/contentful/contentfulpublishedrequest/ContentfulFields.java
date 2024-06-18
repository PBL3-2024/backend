package io.github.pbl32024.model.contentful.contentfulpublishedrequest;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

import java.util.List;

@Data
public class ContentfulFields {

	private String title;

	private String socCodes;

	private List<ExternalLink> externalLinks;

	private ContentfulDescription contentfulDescription;

}
