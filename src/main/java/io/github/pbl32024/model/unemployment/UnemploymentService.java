package io.github.pbl32024.model.unemployment;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnemploymentService {

	private final UnemploymentDAO unemploymentDAO;

	private final BLSClient bLSClient;

	public List<Unemployment> getUnemploymentBySocCode(String socCode) {
		return unemploymentDAO.getUnemploymentBySocCode(socCode);
	}

	@PostConstruct
	@Scheduled(cron = "${backend.unemployment.cron}")
	public void loadUnemploymentData() {
		BLSSeriesRequest blsSeriesRequest = new BLSSeriesRequest();
		blsSeriesRequest.setStartyear("2018");
		blsSeriesRequest.setEndyear(String.valueOf(LocalDate.now().getYear()));
		blsSeriesRequest.setSeriesid(List.of("LNU04000000"));

		BLSSeriesResponse response = bLSClient.fetchBLSSeries(blsSeriesRequest);

		if (!"REQUEST_SUCCEEDED".equals(response.getStatus())) {
			throw new RuntimeException("BLS Request for LNU04000000 Failed");
		}

		List<Unemployment> unemployments = response.getResults().getSeries()
				.stream().filter(s -> "LNU04000000".equals(s.getSeriesID()))
				.flatMap(s -> s.getData().stream())
				.map(data -> {
					Unemployment unemployment = new Unemployment();
					unemployment.setId(data.getYear() + "-" + data.getPeriod());
					unemployment.setDate(LocalDateTime.parse(data.getYear() + "-" + data.getPeriod().substring(1) + "-01T00:00:00"));
					unemployment.setValue(data.getValue());
					unemployment.setSocCode("00-0000");
					return unemployment;
				})
				.sorted(Comparator.comparing(Unemployment::getDate).reversed())
				.limit(36)
				.toList();

		unemploymentDAO.saveAll(unemployments);
	}

}
