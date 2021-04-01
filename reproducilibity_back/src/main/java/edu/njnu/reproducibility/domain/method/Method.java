package edu.njnu.reproducibility.domain.method;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.method.support.Link;
import edu.njnu.reproducibility.domain.method.support.Node;
import edu.njnu.reproducibility.domain.method.support.NodeRelatedItemInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:46
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "Methods")
public class Method extends BaseEntity {
    @Id
    String id;
    String projectId;
    String version;
    String creator;//project creator
    List<Node> nodeList;
    List<Link> linkList;
    List<NodeRelatedItemInfo> nodeRelatedItemInfoList;
}
