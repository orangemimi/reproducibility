package edu.njnu.reproducibility.domain.method.support;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.domain.context.ContextDefinition;
import edu.njnu.reproducibility.domain.projectResource.Resource;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 16:30
 * @modified By：
 * @version: 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible =true,defaultImpl = ContextDefinition.class)
@JsonSubTypes({@JsonSubTypes.Type(value = ContextDefinition.class, name = "contextDefinition")
        , @JsonSubTypes.Type(value = Resource.class, name = "resource"),
})
public class NodeRelatedItemInfo {
    String relatedNodeId;
    String type;
}
