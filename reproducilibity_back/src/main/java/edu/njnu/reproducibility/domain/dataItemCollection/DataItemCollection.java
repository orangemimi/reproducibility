package edu.njnu.reproducibility.domain.dataItemCollection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.dataItemCollection.support.*;
import edu.njnu.reproducibility.domain.resource.support.FileResource;
import edu.njnu.reproducibility.domain.resourceCollection.ResourceCollection;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "format",visible =true,defaultImpl = FileResource.class)
@JsonSubTypes({@JsonSubTypes.Type(value = FileFormat.class, name = "file"),
        @JsonSubTypes.Type(value = ParameterFormat.class, name = "parameter"),
        @JsonSubTypes.Type(value = SharedFileFormat.class, name = "shared_file")
})
public class DataItemCollection extends ResourceCollection {
    @Id
    String id;
    String format;
    String state;//published ;
    ActivityAttribute activityAttribute;
    AgentAttribute agentAttribute;
}
