package io.github.pbl32024.model.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {

	private final CertificationDAO certificationDAO;

	public List<Certification> getCertifications(CertificationQuery query) {
		return null;
	}

	@Scheduled(cron = "${backend.certifications.cron}")
	public void synchronizeCertifications() {

	}

}
