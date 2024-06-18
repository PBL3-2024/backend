package io.github.pbl32024.model.demand;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandService {

	private final DemandDAO demandDAO;

	public Demand getDemand(String socCode) {
		return null;
	}

	public Demand setDemand(String socCode, long value) {
		return null;
	}

}
