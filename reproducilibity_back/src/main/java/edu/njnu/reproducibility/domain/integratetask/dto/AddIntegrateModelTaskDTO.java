package edu.njnu.reproducibility.domain.integratetask.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateModelTask;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  18:39
 * @Version 1.0.0
 */

@Data
public class AddIntegrateModelTaskDTO implements ToDomainConverter<IntegrateModelTask> {
    String projectId;//project id
    String userName;
    String userId;

//    String taskId;
    String taskName;
    String taskDescription;

//    List<Map<String,String>> models;

//    List<ModelAction> modelActions;

//    String xml;
//    String mxgraph;
}
