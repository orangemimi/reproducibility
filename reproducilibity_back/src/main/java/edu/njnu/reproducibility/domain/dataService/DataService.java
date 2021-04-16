package edu.njnu.reproducibility.domain.dataService;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:18
 * @modified By：
 * @version: 1.0.0
 */
@Data
@Document(collection = "DataService")
public class DataService extends BaseEntity {
    @Id
    String id;
    String dataServiceId;
    String name;
    List<String> dataSet;
    String date;
}
