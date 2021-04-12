package edu.njnu.reproducibility.domain.integratetask;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.integratetask.support.Action;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  18:11
 * @Version 1.0.0
 */
@Data
@Document(collection = "IntegratedTask")
public class IntegrateTask extends BaseEntity {
    @Id
    String id;
    String taskName;//自己起的
    String taskDescription;

    String projectId;//project id
    String creator;//creator

//    String tid;//tid--> mangerserver fanhui

    String xml;
    String mxgraph;
    Action action;

    String selectAction;
}
