package io.github.pbl32024.model.employment;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentService {

	private final ForecastingService forecastingService;

	public List<Employment> getEmploymentBySocCode(String socCode) {
		return null;
	}

	@PostConstruct
	public void loadEmployment() {

	}

}
