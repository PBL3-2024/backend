package io.github.pbl32024.model.jobposting.usajobs;

import io.github.pbl32024.model.ExternalLink;
import io.github.pbl32024.model.jobposting.JobPosting;
import io.github.pbl32024.model.jobposting.JobPostingClient;
import io.github.pbl32024.model.jobposting.JobPostingQuery;
import io.github.pbl32024.model.jobposting.JobSource;
import io.github.pbl32024.model.occupation.Occupation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class USAJobsJobPostingClient implements JobPostingClient {

	private final USAJobs usaJobs;

	@Override
	public List<JobPosting> fetchJobPosting(Occupation occupation, JobPostingQuery query) {
		SearchResult response = usaJobs.fetchJobPostings(occupation.getTitle());

		List<JobPosting> jobPostings = new ArrayList<>();
			for (MatchedObjectDescriptorWrapper wrapper : response.getSearchResult().getSearchResultItems()) {
				MatchedObjectDescriptor descriptor = wrapper.getMatchedObjectDescriptor();
				JobPosting jobPosting = getPosting(descriptor);
				jobPostings.add(jobPosting);
			}
		return jobPostings;
	}
	
	private JobPosting getPosting(MatchedObjectDescriptor descriptor) {
		JobPosting jobPosting = new JobPosting();

		jobPosting.setId(descriptor.getPositionID());

		jobPosting.setTitle(descriptor.getPositionTitle());

		String details = getDetails(descriptor);
		jobPosting.setDescription(details);

		jobPosting.setCompany(descriptor.getOrganizationName());

//		if (descriptor.getPositionRemuneration() != null && !descriptor.getPositionRemuneration().isEmpty()) {
			PositionRenumeration remuneration = descriptor.getPositionRemuneration().getFirst();
			jobPosting.setSalaryRange("$"+remuneration.getMinimumRange() + " - $" + remuneration.getMaximumRange());
//		}

		jobPosting.setDeadline(descriptor.getApplicationCloseDate().toString());

		jobPosting.setLocation(descriptor.getPositionLocationDisplay());

		List<ExternalLink> links = new ArrayList<>();
		ExternalLink infoLink = new ExternalLink();
		infoLink.setLabel("Learn More");
		infoLink.setUrl(descriptor.getPositionURI());

		links.add(infoLink);
		descriptor.getApplyURI().stream()
				.map(url -> {
					ExternalLink link = new ExternalLink();
					link.setUrl(url);
					link.setLabel("Apply");
					return link;
				}).forEach(links::add);
		jobPosting.setExternalLink(links);

		jobPosting.setJobSource(JobSource.USA_JOBS);

		return jobPosting;

	}

	private String getDetails(MatchedObjectDescriptor descriptor) {
		Details detailsData = new Details();

		detailsData.setJobSummary(descriptor.getUserArea().getDetails().getJobSummary());
		detailsData.setLowGrade(descriptor.getUserArea().getDetails().getLowGrade());
		detailsData.setHighGrade(descriptor.getUserArea().getDetails().getHighGrade());
		detailsData.setTotalOpenings(descriptor.getUserArea().getDetails().getTotalOpenings());
		detailsData.setMajorDuties(descriptor.getUserArea().getDetails().getMajorDuties());
		detailsData.setEducation(descriptor.getUserArea().getDetails().getEducation());
		detailsData.setRequirement(descriptor.getUserArea().getDetails().getRequirement());


        return "Job Summary: " + detailsData.getJobSummary() + "\n\r" +
				"Low Grade: " + detailsData.getLowGrade() + "\n\r" +
				"High Grade: " + detailsData.getHighGrade() + "\n\r" +
				"Total Openings: " + detailsData.getTotalOpenings() + "\n\r" +
				"Major Duties: " + detailsData.getMajorDuties() + "\n\r" +
				"Education: " + detailsData.getEducation() + "\n\r" +
				"Requirement: " + detailsData.getRequirement();
	}
}

