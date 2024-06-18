package io.github.pbl32024.model.employment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastingService {

	private final ForecastingProperties forecastingProperties;

	public List<Employment> forecast(List<Employment> employment) {
		return null;
	}

}
