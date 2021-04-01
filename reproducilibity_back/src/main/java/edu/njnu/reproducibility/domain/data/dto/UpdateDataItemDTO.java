package edu.njnu.reproducibility.domain.data.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.data.support.DataItem;
import lombok.Data;

import java.util.List;

@Data
public class UpdateDataItemDTO implements ToDomainConverter<DataItem> {
    List<String> toModelInstanceList; //作为输入数据的使用过的instance
    String fromModelInstance;//作为输出数据使用过的instance
    String stepBindId;//stepBind
    String stepBindName;//stepBind
    List<String> stepInherit;//数据在哪些步骤中使用的
}
