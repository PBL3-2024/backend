package io.github.pbl32024.model.analytics;


import lombok.Data;

import java.util.List;

@Data
public class BarChart extends Chart {

	private String type;

	private List<Tuple> data;

	private String xAxisLabel;

	private String yAxisLabel;

	private String markerType;

}
