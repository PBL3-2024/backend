package io.github.pbl32024.model.unemployment;

import lombok.Data;

import java.util.List;

@Data
public class BLSSeriesRequest {

	private String startyear;

	private String endyear;

	private List<String> seriesId;

}
