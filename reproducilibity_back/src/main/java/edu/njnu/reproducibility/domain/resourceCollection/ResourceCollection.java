package edu.njnu.reproducibility.domain.resourceCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 19:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ResourceCollection extends BaseEntity {
    String name;
    String description;
    List<String> keywords;
    String type;//dataItem modelItem
//    String format;//file
    String userId;//
    String projectId;
    String version;
    String value; //url or value    or existing file url /value
}
