package io.github.pbl32024.model.certification;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certifications")
public class CertificationController {

	private final CertificationService certificationService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public CertificationResponse getCertifications(CertificationQuery query) {
		CertificationResponse response = new CertificationResponse();
		response.setCertifications(certificationService.getCertifications(query));
		return response;
	}

}
