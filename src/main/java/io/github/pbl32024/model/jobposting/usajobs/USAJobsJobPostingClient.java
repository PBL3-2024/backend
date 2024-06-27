package io.github.pbl32024.model.jobposting.usajobs;

import io.github.pbl32024.model.ExternalLink;
import io.github.pbl32024.model.jobposting.JobPosting;
import io.github.pbl32024.model.jobposting.JobPostingClient;
import io.github.pbl32024.model.jobposting.JobPostingQuery;
import io.github.pbl32024.model.jobposting.JobSource;
import io.github.pbl32024.model.occupation.Occupation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class USAJobsJobPostingClient implements JobPostingClient {
	private final USAJobs usaJobs;
	@Override
	public List<JobPosting> fetchJobPosting(Occupation occupation, JobPostingQuery query) {

		SearchResult response = usaJobs.fetchJobPostings(occupation.getTitle());

//
//		List<JobPosting> jobPostings = new ArrayList<>();
//
//
//			JobPosting jobPosting = new JobPosting();
//			jobPosting.setId(response.getPositionId());
//			jobPosting.setTitle(response.getPositionTitle());
//			//description
//			jobPosting.setCompany(response.getOrganizationName());
//			jobPosting.setSalaryRange(response.getPositionRemuneration().toString());
//			//jobPosting.setLocation(response.getPositionLocation());
//			//jobPosting.setDeadline(response.getPositionEndDate());
//			jobPosting.setExternalLink(new ExternalLink(descriptor.getPositionUrl()));
//
//			jobPostings.add(jobPosting);
//		}
//		return jobPostings;
		return null;

	}
}

