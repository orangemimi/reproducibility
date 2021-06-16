package edu.njnu.reproducibility.domain.dataItem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import edu.njnu.reproducibility.domain.dataItem.support.*;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "duty",visible =true,defaultImpl = InputParameter.class)
@JsonSubTypes({@JsonSubTypes.Type(value = FileData.class, name = "input")
        , @JsonSubTypes.Type(value = ParameterData.class, name = "parameter")
})
@Document(collection = "ResourceItems")
public class DataItem {
    @Id
    String id;
    String name;
    String description;
    List<String> keywords;
    String duty;//input/parameter

    String format;
    String projectId;
    String userId;//creatorId
    String version;
    SpatialInfo spatialInfo;
    TemporalInfo temporalInfo;
}
