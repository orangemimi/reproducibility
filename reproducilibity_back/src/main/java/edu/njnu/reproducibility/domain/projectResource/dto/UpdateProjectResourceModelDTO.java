package edu.njnu.reproducibility.domain.projectResource.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.projectResource.ProjectResource;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:37
 * @Version 1.0.0
 */
@Data
public class UpdateProjectResourceModelDTO implements ToDomainConverter<ProjectResource> {
    List<String> modelItemCollection;
}
