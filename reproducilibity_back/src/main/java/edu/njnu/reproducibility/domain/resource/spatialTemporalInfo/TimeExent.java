package edu.njnu.reproducibility.domain.resource.spatialTemporalInfo;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:54
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class TimeExent {
    String name;
    String type;
    String value;
    String unit;//second/hour/day/mouth/year
}
