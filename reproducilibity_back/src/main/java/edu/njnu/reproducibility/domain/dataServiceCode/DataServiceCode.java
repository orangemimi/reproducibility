package edu.njnu.reproducibility.domain.dataServiceCode;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.dataServiceCode.support.DependencyItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.InputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.OutputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.ParameterItem;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:18
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "DataServiceCode")
public class DataServiceCode extends BaseEntity {
    @Id
    String id;
    String dataServiceId;
    String name;
    String description;
    String token;
    List<DependencyItem> dependencyItemList;
    List<InputItem> inputItemList;
    List<OutputItem> outputItemList;
    List<ParameterItem> parameterItemList;

    String type;
    String userId;
    String content;
    String contentPath;
}
