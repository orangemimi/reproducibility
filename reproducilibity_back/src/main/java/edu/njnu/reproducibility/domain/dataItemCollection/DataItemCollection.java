package edu.njnu.reproducibility.domain.dataItemCollection;

import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import edu.njnu.reproducibility.domain.dataItemCollection.activityAttribute.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.agentAttribute.AgentAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.restrictionSupport.Restriction;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/15 21:15
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "DataItems")
public class DataItemCollection {
    @Id
    String id;
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
//    Data
}
