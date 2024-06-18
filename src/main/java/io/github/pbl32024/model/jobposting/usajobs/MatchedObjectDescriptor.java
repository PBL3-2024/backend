package io.github.pbl32024.model.jobposting.usajobs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchedObjectDescriptor {

	private String positionId;

	private String positionTitle;

	private String positionLocationDisplay;

	private String organizationName;

	private LocalDateTime positionStartDate;

	private LocalDateTime positionEndDate;

	private LocalDateTime publicationStartDate;

	private LocalDateTime publicationEndDate;

	private String positionUrl;

	private List<PositionLocation> positionLocation;

	private UserArea userArea;

	private List<PositionRenumeration> positionRenumeration;

}
