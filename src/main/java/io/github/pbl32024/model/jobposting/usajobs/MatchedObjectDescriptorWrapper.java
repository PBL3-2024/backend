package io.github.pbl32024.model.jobposting.usajobs;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.PascalCaseStrategy.class)
public class MatchedObjectDescriptorWrapper {
    private MatchedObjectDescriptor matchedObjectDescriptor;
}
