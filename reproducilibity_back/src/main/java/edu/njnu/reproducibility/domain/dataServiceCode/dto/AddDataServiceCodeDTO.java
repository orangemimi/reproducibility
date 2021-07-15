package edu.njnu.reproducibility.domain.dataServiceCode.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.dataService.DataService;
import edu.njnu.reproducibility.domain.dataServiceCode.DataServiceCode;
import edu.njnu.reproducibility.domain.dataServiceCode.support.DependencyItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.InputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.OutputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.ParameterItem;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:16
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddDataServiceCodeDTO implements ToDomainConverter<DataServiceCode> {
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
