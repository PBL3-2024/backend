package io.github.pbl32024.model.employment;


import io.awspring.cloud.s3.S3PathMatchingResourcePatternResolver;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmploymentService {

	private final ForecastingService forecastingService;
	private final Resource[] datasets;

	public EmploymentService(ForecastingService forecastingService, ResourceLoader resourceLoader, @Value("${backend.employment.dataset}") String datasetLocation, S3Client s3Client, ApplicationContext applicationContext) throws IOException {
		this.forecastingService = forecastingService;
		this.datasets = datasetLocation.startsWith("s3")
				? new S3PathMatchingResourcePatternResolver(s3Client, applicationContext).getResources(datasetLocation)
				: ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(datasetLocation);
	}

	public List<Employment> getEmploymentBySocCode(String socCode) {
		return null;
	}

	@PostConstruct
	public void loadEmployment() {
		log.info("Loading employment datasets: {}", (Object) datasets);
		Pattern filenamePattern = Pattern.compile("^.*(\\d{4}-\\d{2}-\\d{2}).csv$");
		for (Resource resource : datasets) {
			Matcher matcher = filenamePattern.matcher(resource.getFilename());
			if (!matcher.find()) {
				log.warn("Could not extract year from dataset {}", resource.getFilename());
				continue;
			}
			LocalDateTime date = LocalDate.parse(matcher.group(1)).atStartOfDay();

			// TODO - Parse CSV here
		}
	}

}
