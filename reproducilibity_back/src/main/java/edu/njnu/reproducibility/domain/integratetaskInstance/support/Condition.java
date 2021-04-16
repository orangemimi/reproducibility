package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 19:56
 * @modified By：
 * @version: 1.0.0
 */

@Data
public class Condition {
    String id;
    String name;
    String description;
    RelatedDataItem relatedDataItem;
    List<Branch> branchList;


}
