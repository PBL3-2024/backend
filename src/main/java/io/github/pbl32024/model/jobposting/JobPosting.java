package io.github.pbl32024.model.jobposting;

import io.github.pbl32024.model.ExternalLink;

public class JobPosting {

	public String id;

	public String title;

	public String description;

	public String company;

	public String salaryRange;

	public String location;

	public String deadline;

	private ExternalLink externalLink;

	private JobSource jobSource;

}
