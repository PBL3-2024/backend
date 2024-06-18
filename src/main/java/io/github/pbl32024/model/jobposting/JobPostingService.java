package io.github.pbl32024.model.jobposting;

import io.github.pbl32024.model.occupation.OccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {

	private final JobPostingClient jobPostingClient;

	private final OccupationService occupationService;

	public List<JobPosting> getJobPosting(JobPostingQuery query) {
		return null;
	}

}
