package edu.njnu.reproducibility.domain.result.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 20:26
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ComparisonEntity {
    ComparisonConclusion conclusion;
    ComparisonMethod method;

}
