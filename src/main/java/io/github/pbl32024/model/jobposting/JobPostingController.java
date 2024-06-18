package io.github.pbl32024.model.jobposting;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

public class JobPostingController {

	private JobPostingService jobPostingService;

	public JobPostingResponse getJobPosting(JobPostingQuery query, SpringDataWebProperties.Sort sort) {
		return null;
	}

}
