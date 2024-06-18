package io.github.pbl32024.model.employment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("backend.forecasting")
public class ForecastingProperties {

	public double arimaP;

	public double arimaD;

	public double arimaQ;

}
