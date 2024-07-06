package io.github.pbl32024.model.employment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
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
	public ResponseEntity<EmploymentResponse> getEmploymentBySocCode(String socCode) {
		try {
			EmploymentResponse response = new EmploymentResponse();
			response.setEmployment(employmentService.getEmploymentBySocCode(socCode));
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
					.body(response);
		} catch (Exception e) {
			EmploymentResponse response = new EmploymentResponse();
			response.setEmployment(List.of());
			return ResponseEntity.ok(response);
		}
	}

}
