package edu.njnu.reproducibility.domain.projectResource;


import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:04
 * @Version 1.0.0
 */
@Data
@Document(collection = "ProjectResources")
public class ProjectResource extends BaseEntity {
    @Id
    String id;
    String userId;
    String projectId;
    List<String> dataItemCollection;//related dataitem ids
    List<String> modelItemCollection;//related modelitem ids
    List<String> relatedDataItemCollection;//related 中间数据 ids

}
