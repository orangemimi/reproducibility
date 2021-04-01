package edu.njnu.reproducibility.domain.method.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.method.Method;
import edu.njnu.reproducibility.domain.method.support.Link;
import edu.njnu.reproducibility.domain.method.support.Node;
import edu.njnu.reproducibility.domain.method.support.NodeRelatedItemInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:47
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateMethodDTO implements ToDomainConverter<Method> {
    List<Node> nodeList;
    List<Link> linkList;
    List<NodeRelatedItemInfo> nodeRelatedItemInfoList;
}
