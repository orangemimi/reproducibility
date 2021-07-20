package edu.njnu.reproducibility.domain.result.support;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.domain.resource.support.FileResource;
import edu.njnu.reproducibility.domain.resource.support.ParameterResource;
import edu.njnu.reproducibility.domain.resource.support.SharedFileResource;
import edu.njnu.reproducibility.domain.resourceCollection.ResourceCollection;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 19:31
 * @modified By：
 * @version: 1.0.0
 */
@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "format",visible =true,defaultImpl = FileResource.class)
//@JsonSubTypes({@JsonSubTypes.Type(value = FileResource.class, name = "file"),
//        @JsonSubTypes.Type(value = ParameterResource.class, name = "parameter"),
//        @JsonSubTypes.Type(value = SharedFileResource.class, name = "shared_file")
//})
public class ResultEntity extends ResourceCollection {
//    String format;
    String conclusion;

}
