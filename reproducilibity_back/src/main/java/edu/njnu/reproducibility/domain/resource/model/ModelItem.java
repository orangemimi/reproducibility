package edu.njnu.reproducibility.domain.resource.model;


import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.resource.model.support.CodeModel;
import edu.njnu.reproducibility.domain.resource.model.support.ServiceModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:08
 * @Version 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible =true,defaultImpl = ServiceModel.class)
@JsonSubTypes({@JsonSubTypes.Type(value = ServiceModel.class, name = "service")
        , @JsonSubTypes.Type(value = CodeModel.class, name = "code"),
})
@Document(collection = "ModelCollection")
public class ModelItem extends BaseEntity {
//    ComputableModel computableModel;
    @Id
    String id;
    String name;
    String description;
    String doi;

    String type;//service/code/exe
    String source;
    String privacy;
    String thumbnail;
    List<String> tag;
    String uploaderId;
}
