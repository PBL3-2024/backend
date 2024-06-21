package io.github.pbl32024.model.employment;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmploymentService {

	private final ForecastingService forecastingService;
	private final Resource[] datasets;

	public EmploymentService(ForecastingService forecastingService, ResourceLoader resourceLoader, @Value("${backend.employment.dataset}") String datasetLocation) throws IOException {
		this.forecastingService = forecastingService;
		this.datasets = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(datasetLocation);
	}

	public List<Employment> getEmploymentBySocCode(String socCode) {
		return null;
	}

	@PostConstruct
	public void loadEmployment() {
		log.info("Loading employment datasets: {}", (Object) datasets);
		for (Resource resource : datasets) {
			LocalDateTime date = LocalDate.parse(resource.getFilename().substring(0, 10)).atStartOfDay();
		}
	}

}
