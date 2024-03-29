package edu.njnu.reproducibility.domain.dataItemCollection.support;

import edu.njnu.reproducibility.domain.dataItemCollection.support.activity.RelatedActivity;
import edu.njnu.reproducibility.domain.dataItemCollection.support.activity.RelatedResource;
import edu.njnu.reproducibility.domain.dataItemCollection.support.activity.RelationshipLink;
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
    RelationshipLink relationshipLink;
}
