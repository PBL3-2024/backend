package io.github.pbl32024.model.unemployment;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unemployment")
public class UnemploymentController {

	private final UnemploymentService unemploymentService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public UnemploymentResponse getUnemploymentBySocCode(String socCode) {
		return null;
	}

}
