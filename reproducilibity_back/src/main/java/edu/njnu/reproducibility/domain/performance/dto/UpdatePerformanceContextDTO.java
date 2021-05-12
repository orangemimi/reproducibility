package edu.njnu.reproducibility.domain.performance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.performance.Completion;
import edu.njnu.reproducibility.domain.performance.Content;
import edu.njnu.reproducibility.domain.performance.Performance;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:44
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdatePerformanceContextDTO implements ToDomainConverter<Content> {
    Content context;

}
