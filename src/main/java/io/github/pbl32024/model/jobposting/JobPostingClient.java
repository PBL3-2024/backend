package io.github.pbl32024.model.jobposting;

import io.github.pbl32024.model.occupation.Occupation;
import org.apache.commons.collections4.functors.PredicateTransformer;

import java.util.List;

public interface JobPostingClient {

	public List<JobPosting> fetchJobPosting(Occupation occupation, JobPostingQuery query);
}
