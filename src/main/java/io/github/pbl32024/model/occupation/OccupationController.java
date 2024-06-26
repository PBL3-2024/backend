package io.github.pbl32024.model.occupation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/occupations")
public class OccupationController {

	private final OccupationService occupationService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Occupation getOccupation(String socCode) {
		return occupationService.getOccupation(socCode);
	}

	@GetMapping(path = "/children/", produces = MediaType.APPLICATION_JSON_VALUE)
	public OccupationResponse getChildOccupations(String socCode) {
		OccupationResponse occupationResponse = new OccupationResponse();
		occupationResponse.setOccupation(occupationService.getChildOccupations(socCode));
		return occupationResponse;
	}

}
