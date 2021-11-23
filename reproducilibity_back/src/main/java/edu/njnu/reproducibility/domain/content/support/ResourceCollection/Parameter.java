package edu.njnu.reproducibility.domain.content.support.ResourceCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/11:13
 * @Description:
 */
@Data
public class Parameter {

    String name;
    String eventName;
    String type;
    String description;
    String value;
}
