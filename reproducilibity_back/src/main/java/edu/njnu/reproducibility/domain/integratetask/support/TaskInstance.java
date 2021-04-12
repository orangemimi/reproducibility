package edu.njnu.reproducibility.domain.integratetask.support;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  18:11
 * @Version 1.0.0
 */
@Data
@Document(collection = "ModelInstance")
public class TaskInstance extends BaseEntity {
    @Id
    String id;
    String name;//model name
    String description;//model description
    String toolId;//tool id
    String toolUrl;

    String md5;

    //模型运行所需的参数 以及
    Integer status; // 0代表未开始，-1代表运行失败，1代表运行成功, 2代表运行超时(不存在运行中状态，省略)
    String tid;// model taskid
    String ip;
    String port;

    // wzh ModelAction
    List<Map<String,Object>> outputData;
    List<Map<String,Object>> inputData;
    Integer iterationNum = 1;
    String step;
}
