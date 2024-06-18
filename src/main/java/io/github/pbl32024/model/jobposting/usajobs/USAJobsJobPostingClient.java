package io.github.pbl32024.model.jobposting.usajobs;

import io.github.pbl32024.model.jobposting.JobPosting;
import io.github.pbl32024.model.jobposting.JobPostingClient;
import io.github.pbl32024.model.jobposting.JobPostingQuery;
import io.github.pbl32024.model.occupation.Occupation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class USAJobsJobPostingClient implements JobPostingClient {

	@Override
	public List<JobPosting> fetchJobPosting(Occupation occupation, JobPostingQuery query) {
		return null;
	}
}
