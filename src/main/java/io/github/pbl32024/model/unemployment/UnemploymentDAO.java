package io.github.pbl32024.model.unemployment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class UnemploymentDAO {

	// TODO connect to database
	private final AtomicReference<List<Unemployment>> unemploymentHolder = new AtomicReference<>();

	public List<Unemployment> getUnemploymentBySocCode(String socCode) {
		return unemploymentHolder.get();
	}

	public void saveAll(List<Unemployment> unemployment) {
		unemploymentHolder.set(unemployment);
	}

}
