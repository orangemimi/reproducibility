package edu.njnu.reproducibility.domain.user.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 17:39
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddUserDTO implements ToDomainConverter<User> {
    String name;
    String password;    //MD5
    String email;   //used for registration
    String userId;

    List<String> joinedProjects;
    List<String> createdProjects;
    List<String> forkedProjects;

}
