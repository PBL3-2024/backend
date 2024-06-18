package io.github.pbl32024.model.employment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employment")
public class EmploymentController {

	private final EmploymentService employmentService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public EmploymentResponse getEmploymentBySocCode(String socCode) {
		return null;
	}

}
