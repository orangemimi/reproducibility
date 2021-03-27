package edu.njnu.reproducibility.domain.resource.data.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.resource.data.DataItem;
import edu.njnu.reproducibility.domain.resource.spatialTemporalInfo.SpatioTemporalInfo;
import lombok.Data;

import java.util.List;

@Data
public class AddDataItemDTO implements ToDomainConverter<DataItem> {
    String name;
    String alia;//别名
    String type;//suffix
    String description;
    String url;
    String source;
    Boolean isDirect; //false--中间处理数据 //true--直接上传数据
    SpatioTemporalInfo spatioTemporalInfo;

    String projectId;
    String uploaderId;
    String fileSize;
    String privacy;     //public/private
    String thumbnail; //缩略图地址
}
