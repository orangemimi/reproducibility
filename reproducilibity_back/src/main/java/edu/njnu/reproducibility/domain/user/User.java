package edu.njnu.reproducibility.domain.user;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

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
    ArrayList<String> organizations;
    ArrayList<String> domain;
    ArrayList<String> loginIp;
    //private ArrayList<String> resource;

    /**
     * Unique
     * adi
     */
    private ArrayList<String> joinedProjects;
    private ArrayList<String> manageProjects;


}
