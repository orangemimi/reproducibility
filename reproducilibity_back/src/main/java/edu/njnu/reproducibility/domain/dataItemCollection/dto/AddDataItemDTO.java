package edu.njnu.reproducibility.domain.dataItemCollection.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.dataItemCollection.activityAttribute.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.agentAttribute.AgentAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.restrictionSupport.Restriction;
import lombok.Data;

import java.util.List;

@Data
public class AddDataItemDTO implements ToDomainConverter<DataItemCollection> {
    String name;
    String description;
    List<String> keywords;
    //    String duty;
    String format;//file/parameter/sharedFile
    String value;//url or value
    String size;

    String projectId;
    String userId;//creatorId

    SpatialInfo spatialInfo;
    TemporalInfo temporalInfo;
    Restriction restriction;

    String version;
    String state;//published ;

    ActivityAttribute activityAttribute;
    AgentAttribute agentAttribute;
}
