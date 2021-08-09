package edu.njnu.reproducibility.domain.context.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.context.ContextDefinition;
import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/9/14  22:28
 * @Version 1.0.0
 */
@Data
public class AddContextDefinitionDTO implements ToDomainConverter<ContextDefinition> {
    String userId;
    String projectId;

    List<String> themes;
    String purpose;
    String objects;//sumulation object
    String methods;
    TemporalInfo temporalInfo;
    SpatialInfo spatialInfo;
    String boundary;
    String discussion;
}
