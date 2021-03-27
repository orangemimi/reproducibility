package edu.njnu.reproducibility.domain.context.output;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  20:44
 * @Version 1.0.0
 */
@Data
@JsonTypeName(value = "output_node")
public class OutputResource {
    String name;
    String description;
    OutputConstraint outputConstraint;
    OutputForm outputForm;//show form
}
