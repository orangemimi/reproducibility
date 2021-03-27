package edu.njnu.reproducibility.domain.context.output;

import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  21:48
 * @Version 1.0.0
 */
@Data
public class OutputForm {
    String form;//定量or定性
    FormEnum formEnum;
}
