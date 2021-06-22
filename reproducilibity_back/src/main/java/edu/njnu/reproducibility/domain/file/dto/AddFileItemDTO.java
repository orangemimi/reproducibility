package edu.njnu.reproducibility.domain.file.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.file.FileItem;
//import edu.njnu.reproducibility.domain.context.support.SpatioTemporalInfo;
import lombok.Data;

@Data
public class AddFileItemDTO implements ToDomainConverter<FileItem> {
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


    //数据在数据容器中的链接 url？
//  String address;

    Boolean folder;

//    private Boolean userUpload;

    //存储父资源的 uuid
    String parent;
}
