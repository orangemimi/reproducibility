package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/12/09/15:28
 * @Description:
 */

@Data
public class Output {
    String state;
    String event;
    String dataId;
    DataContent dataContent;
}
