package edu.njnu.reproducibility.domain.performance;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:42
 * @modified By：
 * @version: 1.0.0
 */

@Data
@Document(collection = "Performance")
public class Performance extends BaseEntity {
    @Id
    String id;
    String projectId;
    Completion completion;//百分比
    String userId;
//    String role;//creator/ rebuilder...
}
