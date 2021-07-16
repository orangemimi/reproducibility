package edu.njnu.reproducibility.domain.projectResource.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.projectResource.ProjectResource;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 20:00
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateResourceRelatedDataDTO implements ToDomainConverter<ProjectResource> {
    List<String> relatedDataItemCollection;
}
