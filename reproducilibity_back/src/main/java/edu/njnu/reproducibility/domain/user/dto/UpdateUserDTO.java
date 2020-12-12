package edu.njnu.reproducibility.domain.user.dto;

import cn.hutool.json.JSONArray;
import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.user.User;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 15:46
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateUserDTO implements ToDomainConverter<User> {

    String name;
    String userName;
    String password;

    String mobilePhone;
    String gender;
    String jobTitle;
    String country;
    String city;
    String organization;
    String introduction;
    String direction;
    String homePage;
    String avatar;

    List<String> joinedProjects;
    List<String> createdProjects;
}
