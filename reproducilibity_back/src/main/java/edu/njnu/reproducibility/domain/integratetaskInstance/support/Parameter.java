package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 19:44
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Parameter {
    String id;
    String key;//event id
    String value;
    String description;

}
