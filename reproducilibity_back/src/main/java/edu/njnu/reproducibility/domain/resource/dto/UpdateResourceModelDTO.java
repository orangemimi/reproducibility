package edu.njnu.reproducibility.domain.resource.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.projectResource.Resource;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:37
 * @Version 1.0.0
 */
@Data
public class UpdateResourceModelDTO implements ToDomainConverter<Resource> {
    List<String> modelItemCollection;
}
