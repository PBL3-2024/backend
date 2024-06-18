package io.github.pbl32024.model.certification;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

import java.util.List;

@Data
public class Certification {

	private String id;

	private List<String> socCode;

	private String title;

	private String description;

	private String organization;

	private List<ExternalLink> externalLinks;

}
