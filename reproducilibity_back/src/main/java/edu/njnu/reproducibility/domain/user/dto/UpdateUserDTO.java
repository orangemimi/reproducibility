package edu.njnu.reproducibility.domain.user.dto;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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
//    String password;

    /**
     * User server
     */
//    String avatar;
    String phone;
    String title;
    String country;
    String state;    // County / State / Province
    String city;
//    String homepage;
//    String introduction;
//    String createdTime;
//    List<String> organizations;
//    List<String> domain;
//    List<String> loginIp;
//
//    List<String> joinedProjects;
//    List<String> createdProjects;
}
