package io.github.pbl32024.model.occupation;

import io.github.pbl32024.model.jobposting.JobPosting;
import io.github.pbl32024.model.jobposting.JobPostingClient;
import io.github.pbl32024.model.jobposting.JobPostingQuery;

import java.util.List;

public class USAJobsJobPostingClient implements JobPostingClient {

	private JobPostingClient jobPostingClient;

	@Override
	public List<JobPosting> fetchJobPosting(Occupation occupation, JobPostingQuery query) {
		return null;
	}
}
