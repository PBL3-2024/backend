package io.github.pbl32024.model.contentful.contentfulpublishedrequest;

import lombok.Data;

import java.util.List;

@Data
public class ContentfulContent {

	private List<ContentfulContent> content;

	private List<String> marks;

	private String value;

	private String nodeType;


}
