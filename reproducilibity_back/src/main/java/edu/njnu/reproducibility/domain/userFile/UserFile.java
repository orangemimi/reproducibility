package edu.njnu.reproducibility.domain.userFile;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/22/13:18
 * @Description:
 */
@Data
@Document(collection = "UserFile")
public class UserFile extends BaseEntity {
    @Id
    String id;
    String name;
    String type;
    String size;
    String fid;
    String storey;
    String url;
    String userId;
    String state;
    List<String> children;
}
