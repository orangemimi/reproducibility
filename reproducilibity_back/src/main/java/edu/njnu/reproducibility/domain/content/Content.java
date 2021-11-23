package edu.njnu.reproducibility.domain.content;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.Context;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Resource;
import edu.njnu.reproducibility.domain.content.support.ScenarioCollection.Scenario;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/10:52
 * @Description:
 */
@Data
@Document(collection = "Content")
public class Content extends BaseEntity {
    @Id
    String id;
    String projectId;
    Context context;
    Resource resource;
    Scenario scenario;
}
