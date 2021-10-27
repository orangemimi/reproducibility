package edu.njnu.reproducibility.domain.integratetaskInstance;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.integratetask.support.Action;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.TaskInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 16:39
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "IntegrateTaskInstance")
public class IntegrateTaskInstance extends BaseEntity {
    @Id
    String id;
    String name;//instance name;
    String taskId;//integrated task id;
    String projectId;
    String operatorId;//operatorId id;

    //integrate task relate
    Action action;
    //模型运行所需的参数 以及
    Integer status; //1表示已update； 0未曾全部update
    // 0代表未开始，-1代表运行失败，1代表运行成功, 2代表运行超时(不存在运行中状态，省略)
    String tid;//managerServer Id;

    //note task relate
//    String content;

    TaskInfo taskInfo;

}
