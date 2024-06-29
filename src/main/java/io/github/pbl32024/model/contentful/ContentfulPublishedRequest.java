package io.github.pbl32024.model.contentful;

import lombok.Data;

@Data
public class ContentfulPublishedRequest {

	private ContentfulFields fields;

	private ContentfulMetadata metadata;

	private ContentfulSys sys;

}
