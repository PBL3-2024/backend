package io.github.pbl32024.model.unemployment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnemploymentService {

	private final UnemploymentDAO unemploymentDAO;

	private final BLSClient bLSClient;

	public List<Unemployment> getUnemploymentBySocCode(String socCode) {
		return null;
	}

	public void loadUnemploymentData() {

	}

}
