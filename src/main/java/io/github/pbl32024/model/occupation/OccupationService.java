package io.github.pbl32024.model.occupation;


import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OccupationService {

	private final OccupationTrie occupationTrie;
	private final Resource socDefinitions;

	public OccupationService(OccupationTrie occupationTrie, @Value("${backend.occupation.soc-definitions}") Resource socDefinitions) {
		this.occupationTrie = occupationTrie;
		this.socDefinitions = socDefinitions;
	}

	public Occupation getOccupation(String socCode) {
		return occupationTrie.getBySocCode(socCode);
	}

	public List<Occupation> getChildOccupations(String socCode) {
		if (socCode == null) {
			return List.of();
		}

		if ("00-0000".equals(socCode)) {
			return occupationTrie.getAll().stream()
					.filter(o -> o.getSocCode().endsWith("0000"))
					.filter(o -> !o.getSocCode().equals(socCode))
					.sorted(Comparator.comparing(Occupation::getTitle))
					.toList();
		}

		if (socCode.endsWith("0000")) {
			return occupationTrie.getWithPrefix(socCode.substring(0, 3)).stream()
					.filter(o -> o.getSocCode().endsWith("00"))
					.filter(o -> !o.getSocCode().equals(socCode))
					.sorted(Comparator.comparing(Occupation::getTitle))
					.toList();
		}

		if (socCode.endsWith("00")) {
			return occupationTrie.getWithPrefix(socCode.substring(0, 5)).stream()
					.filter(o -> o.getSocCode().endsWith("0"))
					.filter(o -> !o.getSocCode().equals(socCode))
					.sorted(Comparator.comparing(Occupation::getTitle))
					.toList();
		}

		if (socCode.endsWith("0")) {
			return occupationTrie.getWithPrefix(socCode.substring(0, 6)).stream()
					.filter(o -> !o.getSocCode().equals(socCode))
					.sorted(Comparator.comparing(Occupation::getTitle))
					.toList();
		}

		return occupationTrie.getWithPrefix(socCode).stream()
				.sorted(Comparator.comparing(Occupation::getTitle))
				.filter(o -> !o.getSocCode().equals(socCode))
				.toList();
	}

	@PostConstruct
	public void loadOccupationTrie() throws Exception {
		try (CSVParser parser = CSVFormat.EXCEL.builder()
				.setSkipHeaderRecord(true)
				.build().parse(new InputStreamReader(socDefinitions.getInputStream()))) {
			List<Occupation> occupations = new ArrayList<>();
			for (CSVRecord record : parser) {
				Occupation occupation = new Occupation();
				occupation.setSocCode(record.get(1));
				occupation.setTitle(record.get(2));
				occupation.setDescription(record.get(3));
				occupations.add(occupation);
			}
			occupationTrie.addAll(occupations);
		}
	}

	public List<Occupation> getAllOccupations() {
		return occupationTrie.getAll();
	}

}
