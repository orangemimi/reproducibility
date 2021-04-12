package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 19:42
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class DataItem {
    String id;//resourceId
    String name;
    String description;
    String reference;
    String key;//linked event id
    String value;
}
