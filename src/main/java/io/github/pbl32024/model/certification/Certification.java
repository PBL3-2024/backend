package io.github.pbl32024.model.certification;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Certification {

	private String id;

	private Set<String> socCode;

	private String title;

	private String description;

	private String source;

	private ExternalLink externalLink;

}
