package io.github.pbl32024.model.unemployment;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnemploymentService {

	private final UnemploymentDAO unemploymentDAO;

	private final BLSClient bLSClient;

	public List<Unemployment> getUnemploymentBySocCode(String socCode) {
		return unemploymentDAO.getUnemploymentBySocCode(socCode);
	}

	@EventListener(ApplicationStartedEvent.class)
	@Scheduled(cron = "${backend.unemployment.cron}")
	public void loadUnemploymentData() {
		BLSSeriesRequest blsSeriesRequest = new BLSSeriesRequest();
		blsSeriesRequest.setStartyear("2018");
		blsSeriesRequest.setEndyear(String.valueOf(LocalDate.now().getYear()));
		blsSeriesRequest.setSeriesid(List.of("LNU04000000"));

		BLSSeriesResponse response;

		try {
			response = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue("""
					{
					  "status": "REQUEST_SUCCEEDED",
					  "responseTime": 126,
					  "message": [],
					  "Results": {
					    "series": [
					      {
					        "seriesID": "LNU04000000",
					        "data": [
					          {
					            "year": "2024",
					            "period": "M05",
					            "periodName": "May",
					            "latest": "true",
					            "value": "3.7",
					            "footnotes": [
					              {}
					            ]
					          },
					          {
					            "year": "2024",
					            "period": "M04",
					            "periodName": "April",
					            "value": "3.5",
					            "footnotes": [
					              {}
					            ]
					          },
					          {
					            "year": "2024",
					            "period": "M03",
					            "periodName": "March",
					            "value": "3.9",
					            "footnotes": [
					              {}
					            ]
					          },
					          {
					            "year": "2024",
					            "period": "M02",
					            "periodName": "February",
					            "value": "4.2",
					            "footnotes": [
					              {}
					            ]
					          },
					          {
					            "year": "2024",
					            "period": "M01",
					            "periodName": "January",
					            "value": "4.1",
					            "footnotes": [
					              {}
					            ]
					          }
					        ]
					      }
					    ]
					  }
					}
					""", BLSSeriesResponse.class);
			//response = bLSClient.fetchBLSSeries(blsSeriesRequest);
		} catch (Exception e) {
			log.warn("Error making BLS request", e);
			response = new BLSSeriesResponse();
			response.setStatus("ERROR");
		}

		if (!"REQUEST_SUCCEEDED".equals(response.getStatus())) {
			log.warn("BLS Request for LUN04000000 Failed");
			return;
		}

		List<Unemployment> unemployments = response.getResults().getSeries()
				.stream().filter(s -> "LNU04000000".equals(s.getSeriesID()))
				.flatMap(s -> s.getData().stream())
				.map(data -> {
					Unemployment unemployment = new Unemployment();
					unemployment.setId(data.getYear() + "-" + data.getPeriod());
					unemployment.setDate(LocalDateTime.parse(data.getYear() + "-" + data.getPeriod().substring(1) + "-01T00:00:00"));
					unemployment.setValue(Double.parseDouble(data.getValue()));
					unemployment.setSocCode("00-0000");
					return unemployment;
				})
				.sorted(Comparator.comparing(Unemployment::getDate).reversed())
				.limit(36)
				.toList();

		unemploymentDAO.saveAll(unemployments);
	}

}
