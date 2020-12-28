package edu.njnu.reproducibility.domain.project.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.project.Creator;
import edu.njnu.reproducibility.domain.project.Member;
import edu.njnu.reproducibility.domain.project.Project;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/10 21:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateProjectDTO implements ToDomainConverter<Project> {
    String name;
    String description;
    String introduction;
    String purpose;
    String privacy;
    String picture;
    List<String> tag;

    List<Member> members;//type: ['builder', 'rebuilder_explorer','rebuilder_operator', 'visitor']

    Integer starCount;
    Integer watchCount;
    Integer folkCount;
}
