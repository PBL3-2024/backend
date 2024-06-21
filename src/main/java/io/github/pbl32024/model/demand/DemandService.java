package io.github.pbl32024.model.demand;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandService {

	private final DemandDAO demandDAO;

	public Demand getDemand(DemandQuery query) {
		return demandDAO.getDemand(query);
	}

	public Demand setDemand(Demand demand) {
		return demandDAO.setDemand(demand);
	}

}
