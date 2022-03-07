package edu.njnu.reproducibility.domain.record.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.record.Record;
import edu.njnu.reproducibility.domain.record.support.UpdateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/11/12 20:21
 * @modified By：
 * @version: 1.0.0
 */

@Data
public class AddRecordDTO implements ToDomainConverter<Record> {
    String projectId;
    String userId;
    List<UpdateType> content = new ArrayList<>();
}
