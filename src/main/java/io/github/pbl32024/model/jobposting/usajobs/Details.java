package io.github.pbl32024.model.jobposting.usajobs;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.PascalCaseStrategy.class)
public class Details {

	private String jobSummary;

	private String lowGrade;

	private String highGrade;

	private String totalOpenings;

	private List<String> majorDuties;

	private String education;

	private String requirement;

}
