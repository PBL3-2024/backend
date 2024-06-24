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
		List<Employment> employments = new ArrayList<>();
		Random random = new Random();
		boolean uptrend = random.nextBoolean();
		long employment = random.nextInt();
		for (int i = 0; i < 10; i++) {
			if (uptrend) {
				employment = (long) (employment * random.nextDouble(0.95, 1.3));
			} else {
				employment = (long) (employment * random.nextDouble(0.6, 1.1));
			}

			Employment e = new Employment();
			e.setSocCode(socCode);
			e.setValue(employment);
			e.setId(UUID.randomUUID().toString());
			e.setDate(LocalDate.of(2018 + i, 1, 1).atStartOfDay());
			e.setForecasted(2018 + i > 2024);
			employments.add(e);
		}

		EmploymentResponse response = new EmploymentResponse();
		response.setEmployment(employments);
		return response;
	}

}
