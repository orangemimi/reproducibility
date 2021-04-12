package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/2 10:04
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Model {
    String id;//model id/doi?
    String name;
    String description;
    String  reference;//model md5
    Behavior behavior;
}
