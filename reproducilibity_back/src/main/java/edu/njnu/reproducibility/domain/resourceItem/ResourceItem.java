package edu.njnu.reproducibility.domain.resourceItem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.domain.resourceItem.parameterSupport.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/15 21:15
 * @modified By：
 * @version: 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible =true,defaultImpl = InputParameter.class)
@JsonSubTypes({@JsonSubTypes.Type(value = FileResource.class, name = "file")
        , @JsonSubTypes.Type(value = ParameterResource.class, name = "parameter")
})
public class ResourceItem  {
    @Id
    String id;
    String name;
    String description;

    String type;//file,data
    String projectId;
    String userId;//creatorId
}
