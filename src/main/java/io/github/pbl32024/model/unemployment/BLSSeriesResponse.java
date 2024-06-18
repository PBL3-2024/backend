package io.github.pbl32024.model.unemployment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BLSSeriesResponse {

	private String status;

	@JsonProperty("Results")
	private BLSSeriesResponseResult results;

}
