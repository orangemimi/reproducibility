package edu.njnu.reproducibility.domain.dataItemCollection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import edu.njnu.reproducibility.domain.dataItemCollection.activityAttribute.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.agentAttribute.AgentAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.restrictionSupport.Restriction;
import edu.njnu.reproducibility.domain.dataItemCollection.support.*;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "format",visible =true,defaultImpl = InputParameter.class)
@JsonSubTypes({@JsonSubTypes.Type(value = InputParameter.class, name = "file"),
         @JsonSubTypes.Type(value = RangeParameter.class, name = "parameter"),
        @JsonSubTypes.Type(value = SliderParameter.class, name = "shared_file")
})
public class DataItemCollection extends BaseEntity {
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


    Restriction restriction;

    String version;
    String state;//published ;

    ActivityAttribute activityAttribute;
    AgentAttribute agentAttribute;
//    Data
}
