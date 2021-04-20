package edu.njnu.reproducibility.domain.performance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.performance.Completion;
import edu.njnu.reproducibility.domain.performance.Performance;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:43
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddPerformanceDTO implements ToDomainConverter<Performance> {
    String projectId;
    Completion completion;//百分比
    String userId;

//    String role;//creator/ rebuilder...
}
