package edu.njnu.reproducibility.domain.context.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.context.ContextDefinition;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/9/14  22:31
 * @Version 1.0.0
 */
@Data
public class UpdateContextDefinitionDTO implements ToDomainConverter<ContextDefinition> {
    List<String> themes;
    String purpose;
    String object;//sumulation object
    String methods;
    String boundary;
    String scale;
    String discussion;
}
