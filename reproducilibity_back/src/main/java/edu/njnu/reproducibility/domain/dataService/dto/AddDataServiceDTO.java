package edu.njnu.reproducibility.domain.dataService.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.dataService.DataService;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:16
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddDataServiceDTO implements ToDomainConverter<DataService> {
    String name;
    String description;
    String dataServiceId;
    String token;
    String type;
    String privacy;
    List<String> tags;

    String userId;
    List<String> fileList;
}
