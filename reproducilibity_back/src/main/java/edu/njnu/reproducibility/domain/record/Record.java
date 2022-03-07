package edu.njnu.reproducibility.domain.record;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.record.support.UpdateType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    String userId;
    String projectId;
    List<UpdateType> content;

}
