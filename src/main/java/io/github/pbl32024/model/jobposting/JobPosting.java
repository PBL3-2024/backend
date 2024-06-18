package io.github.pbl32024.model.jobposting;

import io.github.pbl32024.model.ExternalLink;
import lombok.Data;

@Data
public class JobPosting {

	private String id;

	private String title;

	private String description;

	private String company;

	private String salaryRange;

	private String location;

	private String deadline;

	private ExternalLink externalLink;

	private JobSource jobSource;

}
