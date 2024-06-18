package io.github.pbl32024.model.certification;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CertificationDAO {

	public List<Certification> getCertificationsBySocCode(String socCode) {
		return null;
	}

	public void synchronizeCertifications() {

	}

	public void save(Certification certification) {

	}

}
