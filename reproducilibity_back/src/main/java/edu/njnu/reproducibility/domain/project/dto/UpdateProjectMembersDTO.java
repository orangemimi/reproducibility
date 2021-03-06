package edu.njnu.reproducibility.domain.project.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.project.Member;
import edu.njnu.reproducibility.domain.project.Project;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/25 20:06
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateProjectMembersDTO implements ToDomainConverter<Project> {
    List<Member> members;//type: ['builder', 'rebuilder_explorer','rebuilder_operator', 'visitor']
}
