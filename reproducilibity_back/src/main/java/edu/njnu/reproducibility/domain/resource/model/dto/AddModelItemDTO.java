package edu.njnu.reproducibility.domain.resource.model.dto;


import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.resource.model.ModelItem;
import edu.njnu.reproducibility.domain.resource.model.support.CodeModel;
import edu.njnu.reproducibility.domain.resource.model.support.ServiceModel;
import edu.njnu.reproducibility.domain.resource.tool.ToolItem;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 11:26
 * @modified By：
 * @version:     1.0.0
 */

@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible =true,defaultImpl = ServiceModel.class)
//@JsonSubTypes({@JsonSubTypes.Type(value = ServiceModel.class, name = "service")
//        , @JsonSubTypes.Type(value = CodeModel.class, name = "code"),
//})
@Document(collection = "ModelCollection")
public class AddModelItemDTO implements ToDomainConverter<ModelItem> {
    //    ComputableModel computableModel;

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
