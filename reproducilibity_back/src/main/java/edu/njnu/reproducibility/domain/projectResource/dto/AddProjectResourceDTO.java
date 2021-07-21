package edu.njnu.reproducibility.domain.projectResource.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.projectResource.ProjectResource;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:58
 * @Version 1.0.0
 */
@Data
public class AddProjectResourceDTO implements ToDomainConverter<ProjectResource> {
    String userId;
    String projectId;
//    List<DataItem> dataItemCollection;
//    List<ModelItem> toolItemCollection;
}
