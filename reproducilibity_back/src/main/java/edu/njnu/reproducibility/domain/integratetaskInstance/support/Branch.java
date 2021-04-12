package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 20:01
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Branch {
    @Id
    String id;
    String name;
    String description;
    String relatedId;//model id? data id?
}
