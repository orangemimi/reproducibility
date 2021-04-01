package edu.njnu.reproducibility.domain.record;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2020/11/12 20:20
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "ActionRecord")
public class Record extends BaseEntity {
    @Id
    String id;
    String nodeId;//context/resource.id
    String userId;
    String projectId;
    String description;
    String content;
    String nodeType; //context definition or resource collection or process...

}
