package io.github.pbl32024.model.occupation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/occupations")
public class OccupationController {

	private final OccupationService occupationService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Occupation> getOccupation(String socCode) {
		Occupation occupation = occupationService.getOccupation(socCode);
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
				.body(occupation);
	}

	@GetMapping(path = "/children/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OccupationResponse> getChildOccupations(String socCode) {
		OccupationResponse occupationResponse = new OccupationResponse();
		occupationResponse.setOccupation(occupationService.getChildOccupations(socCode));
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
				.body(occupationResponse);
	}

}
