package io.github.pbl32024.model.unemployment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table(schema = "public", name = "unemployment")
public class Unemployment {

	@Id
	@Column("id")
	private String id;

	@Column("soc")
	private String socCode;

	@Column("date")
	private LocalDateTime date;

	@Column("value")
	private Double value;

}
