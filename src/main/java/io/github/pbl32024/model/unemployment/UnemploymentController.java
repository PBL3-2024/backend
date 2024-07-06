package io.github.pbl32024.model.unemployment;


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
@RequestMapping("/unemployment")
public class UnemploymentController {

	private final UnemploymentService unemploymentService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UnemploymentResponse> getUnemploymentBySocCode(String socCode) {
		UnemploymentResponse unemploymentResponse = new UnemploymentResponse();
		unemploymentResponse.setUnemployment(unemploymentService.getUnemploymentBySocCode(socCode));
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
				.body(unemploymentResponse);
	}

}
