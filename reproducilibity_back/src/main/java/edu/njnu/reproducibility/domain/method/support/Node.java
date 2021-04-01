package edu.njnu.reproducibility.domain.method.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:53
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Node {
    String name;
    String description;
    String type;
    String step;
    String ref;//component_id/model_id
}
