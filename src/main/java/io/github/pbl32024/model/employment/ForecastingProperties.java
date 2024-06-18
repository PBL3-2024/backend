package io.github.pbl32024.model.employment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("backend.forecasting")
public class ForecastingProperties {

	private double arimaP;

	private double arimaD;

	private double arimaQ;

}
