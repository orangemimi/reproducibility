package edu.njnu.reproducibility.domain.construction;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/1 22:41
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Construction extends BaseEntity {
    @Id
    String id;
    String userId;
    String projectId;
    String selectTaskId;

}
