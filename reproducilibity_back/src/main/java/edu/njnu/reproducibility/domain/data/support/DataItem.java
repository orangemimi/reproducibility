package edu.njnu.reproducibility.domain.data.support;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.resource.spatialTemporalInfo.SpatioTemporalInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:34
 * @Version 1.0.0
 */
@Data
@Document(collection = "DataItems")
public class DataItem extends BaseEntity {
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

//    String projectId;
    String uploaderId;
    String fileSize;
    String privacy;     //public/private
    String thumbnail; //缩略图地址


//    @Id
//    String id;
//    String userId;
//    String projectId;
//
//    String name;//dataitem name
//    String url;//dataitem url-->数据容器
//    Boolean isDirect;//false--中间处理数据 //true--直接上传数据
//    List<String> toModelInstanceList; //作为输入数据的使用过的instance
//    String fromModelInstance;//作为输出数据使用过的instance
//    String stepBindId;//stepBind
//    String stepBindName;//stepBind
//    List<String> stepInherit;//数据在哪些步骤中使用的
}
