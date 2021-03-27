package edu.njnu.reproducibility.domain.resource;

import edu.njnu.reproducibility.domain.resource.spatialTemporalInfo.SpatioTemporalInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:34
 * @Version 1.0.0
 */
@Data
public class DataItem {
    @Id
    String id;
    String name;
    String alia;//别名
    String type;//suffix
    String description;
    String url;
    String source;
    Boolean isDirect; //false--中间处理数据 //true--直接上传数据

    SpatioTemporalInfo spatioTemporalInfo;
}
