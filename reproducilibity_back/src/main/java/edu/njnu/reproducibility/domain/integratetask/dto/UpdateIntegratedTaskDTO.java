package edu.njnu.reproducibility.domain.integratetask.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import edu.njnu.reproducibility.domain.integratetask.support.Action;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  18:40
 * @Version 1.0.0
 */
@Data
public class UpdateIntegratedTaskDTO implements ToDomainConverter<IntegrateTask> {
    String taskName;
    String taskDescription;

    String xml;
    String content;

    Action action;
    String selectInstanceId;

}
