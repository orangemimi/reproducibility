package edu.njnu.reproducibility.domain.model.support;

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
public class ModelItem {
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
    String uploaderId;
    String md5;
}
