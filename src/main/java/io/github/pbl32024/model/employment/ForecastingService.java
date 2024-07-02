package io.github.pbl32024.model.employment;

import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ForecastingService {

	private final ForecastingProperties forecastingProperties;

	public List<Employment> forecast(List<Employment> employment) {
		List<Employment> forecasted = new ArrayList<>(employment);
		forecasted.sort(Comparator.comparing(Employment::getDate));

		double[] data = forecasted.stream()
				.mapToDouble(Employment::getValue)
				.toArray();

		int p = 3;
		int d = 0;
		int q = 3;
		int P = 1;
		int D = 1;
		int Q = 0;
		int m = 0;
		int forecastSize = 3;

		ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);

// Obtain forecast result. The structure contains forecasted values and performance metric etc.
		ForecastResult forecastResult = Arima.forecast_arima(data, forecastSize, params);

// Read forecast values
		double[] forecastData = forecastResult.getForecast();

		int year = employment.getLast().getDate().getYear() + 1;
		for (double point : forecastData) {
			Employment forecastedEmployment = new Employment();
			// fill in fields
			forecastedEmployment.setDate(LocalDate.of(year, 5, 1).atStartOfDay());
			forecastedEmployment.setValue((long) point);
			forecastedEmployment.setSocCode(employment.get(0).getSocCode());
			forecastedEmployment.setId(UUID.randomUUID().toString());
			forecastedEmployment.setForecasted(true);
			forecasted.add(forecastedEmployment);
			year++;
		}

		return forecasted;
	}

}
