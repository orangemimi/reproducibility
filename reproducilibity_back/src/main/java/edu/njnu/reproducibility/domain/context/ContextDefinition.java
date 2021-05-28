package edu.njnu.reproducibility.domain.context;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  13:53
 * @Version 1.0.0
 */
@Data
@Document(collection = "Context")
public class ContextDefinition extends BaseEntity {
    @Id
    String id;
    String userId;
    String projectId;

    List<String> themes;
    String purpose;
    String objects;//sumulation object
    String methods;
    List<TemporalInfo> temporalInfoList;
    List<SpatialInfo> spatialInfoList;
    String boundary;
    String discussion;
}
