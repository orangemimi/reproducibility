package edu.njnu.reproducibility.domain.user;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:28
 * @modified By：
 * @version: 1.0.0
 */

@Data
@Document(collection = "User")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    String userId;  //UUID
    String name;
    String password;    //MD5
    String email;   //used for registration

    /**
     * User server
     */
    String avatar;
    String phone;
    String title;
    String country;
    String province;    // County / State / Province
    String city;
    String homepage;
    String introduction;
    String createdTime;
    List<String> organizations;
    List<String> domain;
    List<String> loginIp;
    //private ArrayList<String> resource;

    /**
     * Unique
     * adi
     */
    List<String> joinedProjects;
    List<String> createdProjects;
    List<String> forkedProjects;
}
