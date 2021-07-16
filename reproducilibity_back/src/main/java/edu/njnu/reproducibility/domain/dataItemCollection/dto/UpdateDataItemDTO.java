package edu.njnu.reproducibility.domain.dataItemCollection.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.dataItemCollection.support.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.support.AgentAttribute;
import edu.njnu.reproducibility.domain.restriction.Restriction;
import lombok.Data;

import java.util.List;

@Data
public class UpdateDataItemDTO implements ToDomainConverter<DataItemCollection> {
    String name;
    String description;
    List<String> keywords;
    //    String duty;
    String format;//file/parameter/sharedFile
    String value;//url or value
    String size;

    String projectId;
    String userId;//creatorId


    Restriction restriction;

    String version;
    String state;//published ;

    ActivityAttribute activityAttribute;
    AgentAttribute agentAttribute;
}
