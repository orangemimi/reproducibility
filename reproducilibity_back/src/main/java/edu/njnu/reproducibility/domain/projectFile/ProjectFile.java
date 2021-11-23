package edu.njnu.reproducibility.domain.projectFile;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/18/13:10
 * @Description:
 */
@Data
@Document(collection = "ProjectFile")
public class ProjectFile extends BaseEntity {
    @Id
    String id;
    String projectId;
    String type;
    String dataType;
    String name;
    String description;
    String value;
    String fileId;
}
