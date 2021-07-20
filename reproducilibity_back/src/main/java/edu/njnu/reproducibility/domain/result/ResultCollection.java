package edu.njnu.reproducibility.domain.result;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.resourceCollection.ResourceCollection;
import edu.njnu.reproducibility.domain.result.support.ComparisonEntity;
import edu.njnu.reproducibility.domain.result.support.ResultEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 19:19
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "ResultItems")
public class ResultCollection extends ResourceCollection {
    @Id
    String id;
    String conclusion;
    ComparisonEntity comparisonEntity;
}
