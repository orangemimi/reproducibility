package edu.njnu.reproducibility.domain.user.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.user.User;
import lombok.Data;

import javax.swing.text.TabableView;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/25 21:22
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateCreatedProjectsDTO implements ToDomainConverter<User> {

    List<String> createdProjects;
}
