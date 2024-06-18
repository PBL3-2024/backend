package io.github.pbl32024.model.jobposting.usajobs;

import java.time.LocalDateTime;
public class MatchedObjectDescriptor {

	public String positionId;

	public String positionTitle;

	public String positionLocationDisplay;

	public String organizationName;

	public LocalDateTime positionStartDate;

	public LocalDateTime positionEndDate;

	public LocalDateTime publicationStartDate;

	public LocalDateTime publicationEndDate;

	public String positionUrl;

	private PositionLocation[] positionLocation;

	private UserArea userArea;

	private PositionRenumeration[] positionRenumeration;

}
