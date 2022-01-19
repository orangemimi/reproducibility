package edu.njnu.reproducibility.domain.project.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/17/22:27
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    String taskContent;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updateDate;
}
