package io.github.pbl32024.model.jobposting.usajobs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.PascalCaseStrategy.class)
public class MatchedObjectDescriptor {

	private String positionID;

	private String positionTitle;

	private String positionLocationDisplay;

	private String organizationName;

	private LocalDateTime positionStartDate;

	private LocalDateTime positionEndDate;

	private LocalDateTime publicationStartDate;

	private LocalDateTime publicationEndDate;

	private LocalDateTime ApplicationCloseDate;

	private String positionURI;

	private List<String> applyURI;

	private List<PositionLocation> positionLocation;

	private UserArea userArea;

	private List<PositionRenumeration> positionRemuneration;

}
