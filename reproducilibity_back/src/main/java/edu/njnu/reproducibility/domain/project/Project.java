package edu.njnu.reproducibility.domain.project;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import edu.njnu.reproducibility.domain.project.support.Citation;
import edu.njnu.reproducibility.domain.project.support.Record;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:44
 * @modified By：
 * @version: 1.0.0
 */

@Data
@Document(collection = "Project")
public class Project extends BaseEntity {
    @Id
    String id;
    String name;
    String description;
    String introduction;
    String purpose;
    String privacy;
    String picture;
    List<String> tags;

//    Creator creator;
    String creator;
    List<Member> members;//role: ['builder', 'rebuilder_explorer','rebuilder_operator']
    Integer level=0;


    Integer starCount;
    Integer watchCount;
    Integer folkCount;
    Citation citation;

    //fork detail
//    Boolean isForking; //是否从别的项目Fork出来
    String forkingProjectId;
    Integer forkRecordId;       //从项目的那条记录开始fork的
    List<Record> records;
//    Boolean isForked;//是否被别的项目Fork
    List<String> forkedProjectIdList;

    //不管是否被Fork 始终是一个Project 以ProjectId为标识


}
