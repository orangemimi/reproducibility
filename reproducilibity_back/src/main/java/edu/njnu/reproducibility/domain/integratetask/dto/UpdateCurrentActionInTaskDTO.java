package edu.njnu.reproducibility.domain.integratetask.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/9 22:43
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateCurrentActionInTaskDTO implements ToDomainConverter<IntegrateTask> {
    String selectAction;
}
