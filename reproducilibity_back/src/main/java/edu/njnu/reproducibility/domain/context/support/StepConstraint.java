package edu.njnu.reproducibility.domain.context.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:47
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class  StepConstraint {
    String name;//T
    String value;//[20,80]
    String unit;//day
}
