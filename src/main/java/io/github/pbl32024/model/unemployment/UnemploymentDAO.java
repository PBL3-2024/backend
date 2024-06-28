package io.github.pbl32024.model.unemployment;


import io.github.pbl32024.SOCSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class UnemploymentDAO {

	private final UnemploymentRepository unemploymentRepository;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public List<Unemployment> getUnemploymentBySocCode(String socCode) {
		return unemploymentRepository.findBySocCodeStartsWith(SOCSupport.trimSoc(socCode));
	}

	@Transactional
	public void saveAll(List<Unemployment> unemployment) {
		SqlParameterSource[] sources = unemployment.stream()
				.map(u -> {
					MapSqlParameterSource params = new MapSqlParameterSource();
					params.addValue("id", u.getId());
					params.addValue("soc", u.getSocCode());
					params.addValue("date", u.getDate());
					params.addValue("value", u.getValue());
					return params;
				})
				.toArray(SqlParameterSource[]::new);

		jdbcTemplate.batchUpdate("""
			INSERT INTO unemployment (id, soc, date, value)
			VALUES (:id, :soc, :date, :value)
			ON CONFLICT (id) DO UPDATE
				SET soc = EXCLUDED.soc,
				date = EXCLUDED.date,
				value = EXCLUDED.value;
        """, sources);
	}

}
