package edu.njnu.reproducibility.domain.integratetask;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/21 16:47
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "Test")
public class test {
    String value;
}
