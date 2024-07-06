package io.github.pbl32024.model.analytics;


import lombok.Data;

import java.util.List;

@Data
public class MapChart extends Chart {

	private String type;

	private List<Tuple> data;

}
