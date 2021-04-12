package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import edu.njnu.reproducibility.domain.method.support.From;
import edu.njnu.reproducibility.domain.method.support.To;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 20:03
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Dependency {
    String id;
    String name;
    From from;
    To to;
}
