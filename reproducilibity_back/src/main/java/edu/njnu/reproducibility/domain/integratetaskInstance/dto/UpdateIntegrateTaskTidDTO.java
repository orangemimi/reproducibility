package edu.njnu.reproducibility.domain.integratetaskInstance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:07
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateIntegrateTaskTidDTO implements ToDomainConverter<IntegrateTask> {
    String tid;
}
