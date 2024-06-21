package io.github.pbl32024.model.demand;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DemandDAO {
	private final Map<String,Demand> map= new ConcurrentHashMap<>(); //"we dont have a database so make a map"

	public Demand getDemand(DemandQuery query) {
		return map.get(query.getSocCode());
	}

	public Demand setDemand(Demand demand) {
		map.put(demand.getSocCode(),demand);
		return demand;
	}

	public void save(Demand demand) {

	}

}
