package io.github.pbl32024.model.demand;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demand")
public class DemandController {

	private final DemandService demandService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Demand getDemand(DemandQuery query) {
		return demandService.getDemand(query);
	}

	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Demand setDemand(@RequestBody Demand demand) {
		demandService.setDemand(demand);
		return demand;
	}

}
