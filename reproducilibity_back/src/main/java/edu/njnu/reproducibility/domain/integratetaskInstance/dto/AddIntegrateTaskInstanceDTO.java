package edu.njnu.reproducibility.domain.integratetaskInstance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.support.Action;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstance;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 20:11
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddIntegrateTaskInstanceDTO implements ToDomainConverter<IntegrateTaskInstance> {
    String name;//instance name;
    String taskId;//integrated task id;
    String operatorId;//operatorId id;

    //integrate task relate
    Action action;
    //模型运行所需的参数 以及
    Integer status; //1表示已update； 0未曾全部update
    // 0代表未开始，-1代表运行失败，1代表运行成功, 2代表运行超时(不存在运行中状态，省略)
    String tid;//managerServer Id;

    //note task relate
//    String content;



}
