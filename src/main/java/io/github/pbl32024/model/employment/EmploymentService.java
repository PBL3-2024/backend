package io.github.pbl32024.model.employment;


import io.awspring.cloud.s3.S3PathMatchingResourcePatternResolver;
import io.github.pbl32024.model.occupation.Occupation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmploymentService {

	private final Map<String, List<Employment>> employmentData = new ConcurrentHashMap<>();
	private final ForecastingService forecastingService;
	private final Resource[] datasets;

	public EmploymentService(ForecastingService forecastingService, ResourceLoader resourceLoader, @Value("${backend.employment.dataset}") String datasetLocation, S3Client s3Client, ApplicationContext applicationContext) throws IOException {
		this.forecastingService = forecastingService;
		this.datasets = datasetLocation.startsWith("s3")
				? new S3PathMatchingResourcePatternResolver(s3Client, applicationContext).getResources(datasetLocation)
				: ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(datasetLocation);
	}

	public List<Employment> getEmploymentBySocCode(String socCode) {
		return forecastingService.forecast(employmentData.get(socCode));
	}

	@PostConstruct
	public void loadEmployment() throws IOException {
		log.info("Loading employment datasets: {}", (Object) datasets);
		Pattern filenamePattern = Pattern.compile("^.*(\\d{4}-\\d{2}-\\d{2}).csv$");
		for (Resource resource : datasets) {
			Matcher matcher = filenamePattern.matcher(resource.getFilename());
			if (!matcher.find()) {
				log.warn("Could not extract year from dataset {}", resource.getFilename());
				continue;
			}
			LocalDateTime date = LocalDate.parse(matcher.group(1)).atStartOfDay();

			try (CSVParser parser = CSVFormat.EXCEL.builder()
					.setHeader()
					.setSkipHeaderRecord(true)
					.build().parse(new InputStreamReader(new BOMInputStream(resource.getInputStream())))) {
				for (CSVRecord record : parser) {
					Employment employment = new Employment();
					employment.setId(UUID.randomUUID().toString());
					employment.setDate(date);
					employment.setSocCode(record.get("OCC_CODE"));

					try {
						employment.setValue(Long.parseLong(record.get("TOT_EMP").replace(",", "")));
					} catch (NumberFormatException e) {
						employment.setValue(0);
					}
					employmentData.computeIfAbsent(employment.getSocCode(), s -> new ArrayList<>()).add(employment);
				}
			}
		}
	}

}
