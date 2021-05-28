package edu.njnu.reproducibility.domain.context.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:54
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class TemporalExtent {
    String name;
    String type;
    String from;
    String to;
    String unit;//second/hour/day/mouth/year
}
