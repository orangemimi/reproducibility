package edu.njnu.reproducibility.domain.integratetask.support;

import edu.njnu.reproducibility.domain.integratetaskInstance.support.*;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:02
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Action {
    List<Model> modelItemList;
    List<DataItem> dataItemList;
    List<Parameter> parameterList;
    List<Condition> conditionList;
    List<Dependency> dependencyList;
}

