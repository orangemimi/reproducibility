package edu.njnu.reproducibility.domain.modelItemCollection;

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
public class ModelItemColletion {
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
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemColletion.java
    String userId;//creator Id
    String md5;
=======
    String uploaderId;
>>>>>>> parent of f11cd19 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/support/ModelItem.java
}
