package io.github.pbl32024.model.jobposting;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobPostingController {

	private final JobPostingService jobPostingService;


	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public JobPostingResponse getJobPosting(JobPostingQuery query) {

		return null;
	}

}
