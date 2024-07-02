package io.github.pbl32024.model.employment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employment")
public class EmploymentController {

	private final EmploymentService employmentService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public EmploymentResponse getEmploymentBySocCode(String socCode) {
		try {
			EmploymentResponse response = new EmploymentResponse();
			response.setEmployment(employmentService.getEmploymentBySocCode(socCode));
			return response;
		} catch (Exception e) {
			EmploymentResponse response = new EmploymentResponse();
			response.setEmployment(List.of());
			return response;
		}
	}

}
