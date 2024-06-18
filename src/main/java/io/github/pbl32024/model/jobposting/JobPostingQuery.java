package io.github.pbl32024.model.jobposting;

import lombok.Data;

@Data
public class JobPostingQuery {

	private String socCode;

	private String userQuery;

	private String yearlySalaryUpper;

	private String yearlySalaryLower;

	private JobSource[] jobSource;

}
