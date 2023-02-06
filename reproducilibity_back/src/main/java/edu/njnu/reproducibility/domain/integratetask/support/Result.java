package edu.njnu.reproducibility.domain.integratetask.support;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
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
public class Result {
    List<ResultValidation> resultValidations = new ArrayList<>();
    ResultValidation resultVisualization  = new ResultValidation();
    Conclusion conclusion = new Conclusion();
}

