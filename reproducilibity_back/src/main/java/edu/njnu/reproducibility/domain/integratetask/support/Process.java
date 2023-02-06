package edu.njnu.reproducibility.domain.integratetask.support;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:02
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Process {
    String name;
    String description;
    String stepType;
    String operateType;
    List<String> references;
    String other;
    List<String> pictures;
    DataItemCollection modelResource;
    List<DataItemCollection> modelInputs;
    List<DataItemCollection> parameters;
    List<DataItemCollection> modelOutputs;

}

