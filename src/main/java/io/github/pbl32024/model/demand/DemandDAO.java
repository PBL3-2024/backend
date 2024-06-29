package io.github.pbl32024.model.demand;


import io.github.pbl32024.SOCSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DemandDAO {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public Demand getDemand(DemandQuery query) {
		Long demand = jdbcTemplate.queryForObject("""
				SELECT SUM(value) AS total_value
				FROM demand
				WHERE soc LIKE CONCAT(:soc, '%');
				""", Map.of("soc", SOCSupport.trimSoc(query.getSocCode())), Long.class);

		Demand result = new Demand();
		result.setSocCode(query.getSocCode());
		result.setValue(demand);
		return result;
	}

	public Demand setDemand(Demand demand) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("soc", demand.getSocCode());
		parameterSource.addValue("value", demand.getValue());
		jdbcTemplate.update("""
				INSERT INTO demand (soc, value)
				VALUES (:soc, :value)
				ON CONFLICT (soc) DO UPDATE
				SET VALUE = :value;
				""", parameterSource);
		return demand;
	}

}
