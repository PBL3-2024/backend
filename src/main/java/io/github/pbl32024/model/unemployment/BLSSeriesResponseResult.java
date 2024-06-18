package io.github.pbl32024.model.unemployment;

import lombok.Data;

import java.util.List;

@Data
public class BLSSeriesResponseResult {

	private List<BLSSeriesResponseResultSeries> series;

}
