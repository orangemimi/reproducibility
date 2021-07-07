package edu.njnu.reproducibility.domain.file;

import edu.njnu.reproducibility.common.entity.BaseEntity;
//import edu.njnu.reproducibility.domain.context.support.SpatioTemporalInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:34
 * @Version 1.0.0
 */
@Data
@Document(collection = "FileItems")
public class FileItem extends BaseEntity {
    @Id
    String id;
    String name;
    String alia;//别名
    String suffix;//suffix
    String description;

//    String url;
    String address;//原始url

    String source;
    Boolean userUpload; //false--中间处理数据 //true--直接上传数据
//    SpatioTemporalInfo spatioTemporalInfo;

//    String projectId;
    String uploaderId;
    String fileSize;
    String privacy;     //public/private
    String thumbnail; //缩略图地址


    Boolean folder;//是否是文件夹
//    Boolean hasChildren;
    //存储父资源的 uuid
    String parent;
    ArrayList<FileItem> children;




}
