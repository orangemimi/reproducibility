package edu.njnu.reproducibility.domain.user;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JwtUtils;
import edu.njnu.reproducibility.domain.user.dto.AddUserDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.njnu.reproducibility.utils.Utils.filterUserInfo;
import static edu.njnu.reproducibility.utils.Utils.filterUserInfoProjects;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:29
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User findByName(String s) {
        return userRepository.findByName(s).orElse(null);
    }

    public JSONArray getUserInfoLike(String value) {
        List<User> userList =userRepository.findByEmailLike(value);
        return filterUserInfo(userList);
    }

    public User register(AddUserDTO add) {
        JSONObject userObj = JSONUtil.parseObj(add);
        if (userRepository.findByEmail(userObj.getStr("email")).isPresent()) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        User user = new User();
        add.convertTo(user);
        return userRepository.insert(user);
    }

    public User updateByUserId(String userId, UpdateUserDTO update) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        update.updateTo(userFromDB);
        return userRepository.save(userFromDB);
    }

    public User updateJoinedProjects(String userId, String update) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        userFromDB.getJoinedProjects().add(update);
//        update.updateTo(userFromDB);
        return userRepository.save(userFromDB);
    }

    public User updateCreatedProjects(String userId, String update) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        userFromDB.getCreatedProjects().add(update);
//        update.updateTo(userFromDB);
        return userRepository.save(userFromDB);
    }

    public User updateFolkedProjects(String userId, String update) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        userFromDB.getForkedProjects().add(update);
//        update.updateTo(userFromDB);
        return userRepository.save(userFromDB);
    }



    public JSONObject login(String email, String password) {
        User userFromDB = userRepository.findByEmail(email).orElseThrow(MyException::noObject);
        if (userFromDB.getPassword().equals(password)) {
            String jwtToken = JwtUtils.generateToken(userFromDB.getUserId(), userFromDB.getName(), password, userFromDB.getAvatar());
            JSONObject json = new JSONObject();
            json.put("name", userFromDB.getName());
            json.put("userId", userFromDB.getUserId());
            json.put("avatar", userFromDB.getAvatar());
            json.put("joinedProjects", userFromDB.getJoinedProjects());
            json.put("createdProjects", userFromDB.getCreatedProjects());
            json.put("token", "Bearer" + " " + jwtToken);

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("token","Bearer" + " " + jwtToken);
//            jsonObject.put("userInfo",json);
            return json;
        } else {
            throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
        }

    }

    public User forgetPassword(String email, String password) {
        User userFromDB = userRepository.findByEmail(email).orElseThrow(MyException::noObject);
        userFromDB.builder().password(password).build();
        return userRepository.save(userFromDB);
    }

        //JUST GET THE PROJECTS INFO
    public JSONObject getUserProjectInfo(String userId) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        return filterUserInfoProjects(userFromDB);
    }

    public User getUserInfoById(String userId) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        return userFromDB;
    }

    public User update(User user){
        return userRepository.save(user);
    }



}
