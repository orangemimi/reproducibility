package edu.njnu.reproducibility.domain.project.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.project.Creator;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.project.support.Record;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/10 21:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddProjectDTO implements ToDomainConverter<Project> {
    String name;
    String description;
    String introduction;
    String purpose;
    String privacy;
    String picture;
    List<String> tags;

    String creator;
    List<String> members = new ArrayList<>();
    Integer level=0;
    String citation;


    Integer starCount = 0;
    Integer watchCount = 0;
    Integer folkCount = 0;

    //fork detail
    String forkingProjectId;
    List<String> forkedProjectIdList = new ArrayList<>();
    List<Record> records = new ArrayList<>();
}
