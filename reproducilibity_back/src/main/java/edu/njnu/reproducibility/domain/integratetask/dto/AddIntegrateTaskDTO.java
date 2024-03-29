package edu.njnu.reproducibility.domain.integratetask.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  18:39
 * @Version 1.0.0
 */

@Data
public class AddIntegrateTaskDTO implements ToDomainConverter<IntegrateTask> {
    String projectId;//project id
    String creator;

    String taskName;
    String taskDescription;

    String taskContent;
    String selectInstanceId;
//    String type;//Integrate Task; notebook

}
