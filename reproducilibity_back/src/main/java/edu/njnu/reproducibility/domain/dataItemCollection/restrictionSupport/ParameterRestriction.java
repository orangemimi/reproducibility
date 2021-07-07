package edu.njnu.reproducibility.domain.dataItemCollection.restrictionSupport;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/7 10:49
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ParameterRestriction extends Restriction {
    String type;// string date number
    String decimal;

    Unit unit;
}
