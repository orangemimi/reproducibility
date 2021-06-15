package edu.njnu.reproducibility.domain.integratetaskInstance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstance;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/8 13:01
 * @modified By：
 * @version: 1.0.0
 */
public class UpdateNoteTaskInstanceDTO implements ToDomainConverter<IntegrateTaskInstance> {
    String content;
}
