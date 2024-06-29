package io.github.pbl32024.model.demand;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(schema = "public", name = "demand")
public class Demand {

	@Id
	@Column("soc")
	private String socCode;

	@Column("value")
	private long value;

}
