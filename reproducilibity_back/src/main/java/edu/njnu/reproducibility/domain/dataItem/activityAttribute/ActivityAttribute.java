package edu.njnu.reproducibility.domain.dataItem.activityAttribute;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/16 22:25
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ActivityAttribute {
    String id;
    RelatedActivity relatedActivity;
    RelatedResource relatedResource;
    Relationship relationship;
}
