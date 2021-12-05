package edu.njnu.reproducibility.domain.modelItemCollection.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.modelItemCollection.ModelItemColletion;
import lombok.Data;

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

public class AddModelItemDTO implements ToDomainConverter<ModelItemColletion> {
    //    ComputableModel computableModel;

    String name;
    String description;
    String doi;

    String type;//service/code/exe
    String source;
    String privacy;
    String thumbnail;
    List<String> tags;
    String uploaderId;
    String md5;
    String image;
    String projectId;
}
