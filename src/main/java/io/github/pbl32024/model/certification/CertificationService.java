package io.github.pbl32024.model.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {

	private final CertificationDAO certificationDAO;

	public List<Certification> getCertifications(CertificationQuery query) {
		return certificationDAO.getCertificationsBySocCode(query.getSocCode());
	}

	@EventListener(ApplicationStartedEvent.class)
	public void synchronizeCertifications() {
		certificationDAO.synchronizeCertifications();
	}

}
