package edu.njnu.reproducibility.domain.record.dto;


import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.record.Record;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2020/11/12 20:21
 * @modified By：
 * @version: 1.0.0
 */

@Data
public class AddRecordDTO implements ToDomainConverter<Record> {
    String userId;
    String projectId;
    String description;
    String content;
    String eventType; //context definition or resource collection or process...
    String eventId; //查询条件Id
}
