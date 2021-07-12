package edu.njnu.reproducibility.domain.resourceCollection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.resource.support.FileResource;
import edu.njnu.reproducibility.domain.resource.support.ParameterResource;
import edu.njnu.reproducibility.domain.resource.support.SharedFileResource;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 19:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "format",visible =true,defaultImpl = FileResource.class)
@JsonSubTypes({@JsonSubTypes.Type(value = FileResource.class, name = "file"),
        @JsonSubTypes.Type(value = ParameterResource.class, name = "parameter"),
        @JsonSubTypes.Type(value = SharedFileResource.class, name = "shared_file")
})
public class ResourceCollection extends BaseEntity {
    String name;
    String description;
    List<String> keywords;
    String duty;//output
    String format;//file
    String userId;//
    String projectId;
    String version;
    String value; //url or value    or existing file url /value
}
