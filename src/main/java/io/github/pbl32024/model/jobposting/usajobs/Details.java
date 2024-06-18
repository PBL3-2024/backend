package io.github.pbl32024.model.jobposting.usajobs;

import lombok.Data;

import java.util.List;

@Data
public class Details {

	private String jobSummary;

	private String lowGrade;

	private String highGrade;

	private String totalOpenings;

	private List<String> majorDuties;

	private String education;

	private String requirement;

}
