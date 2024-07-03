package io.github.pbl32024.model.jobposting.usajobs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategy.PascalCaseStrategy.class)
class PositionLocation {

	private String locationName;

	private String countryCode;

	private String countrySubDivisionCode;

	private String cityName;
}