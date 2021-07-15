package edu.njnu.reproducibility.domain.construction.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.construction.Construction;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/1 22:42
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateConstructionDTO implements ToDomainConverter<Construction> {
    String selectTaskId;
    String type;
}
