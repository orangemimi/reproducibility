package edu.njnu.reproducibility.domain.modelItemCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 21:24
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "ModelItems")
public class ModelItemColletion extends BaseEntity {
    @Id
    String id;
    String name;
    String description;
    String doi;

    String type;//service/code/exe
    String source;
    String privacy;
    String thumbnail;
    List<String> tags;
    String userId;//creator Id
    String md5;
    String image;
    String projectId;
}
