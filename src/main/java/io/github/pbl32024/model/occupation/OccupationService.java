package io.github.pbl32024.model.occupation;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class OccupationService {

	private final OccupationTrie occupationTrie;
	private final Resource socDefinitions;

	public OccupationService(OccupationTrie occupationTrie, @Value("classpath:soc_2018_definitions.csv") Resource socDefinitions) {
		this.occupationTrie = occupationTrie;
		this.socDefinitions = socDefinitions;
	}

	public Occupation getOccupation(String socCode) {
		return occupationTrie.getBySocCode(socCode);
	}

	public List<Occupation> getChildOccupations(String socCode) {
		return null;
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
		return null;
	}

}
