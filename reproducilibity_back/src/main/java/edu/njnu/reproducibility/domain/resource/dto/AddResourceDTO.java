package edu.njnu.reproducibility.domain.resource.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.resource.Resource;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:58
 * @Version 1.0.0
 */
@Data
public class AddResourceDTO implements ToDomainConverter<Resource> {
    String userId;
    String projectId;
//    List<DataItem> dataItemCollection;
//    List<ModelItem> toolItemCollection;
}
