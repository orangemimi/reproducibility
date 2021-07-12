package edu.njnu.reproducibility.domain.resource;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.dataItemCollection.support.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.support.AgentAttribute;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:04
 * @Version 1.0.0
 */
@Data
@Document(collection = "Resources")
public class Resource extends BaseEntity {
    @Id
    String id;
    String name;
    String description;
    List<String> keywords;
    //    String duty;
    String format;//file/parameter/sharedFile

    String value;//url or value

    String projectId;
    String userId;//creatorId

//    Restriction restriction;
    String version;
    String state;//published ;

    ActivityAttribute activityAttribute;
    AgentAttribute agentAttribute;


}
