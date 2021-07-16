package edu.njnu.reproducibility.domain.scenario;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/1 22:41
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "Scenario")
public class Scenario extends BaseEntity {
    @Id
    String id;
    String userId;
    String projectId;
    String selectTaskId;
    String type;//notebook or integrateTask

}
