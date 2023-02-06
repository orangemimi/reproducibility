package edu.njnu.reproducibility.domain.integratetask.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/13/15:35
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultOutput {
    String name = "";
    String description = "";
    String time = "";
    String storage = "";
    String format = "";
    String isUpload = "false";
}
