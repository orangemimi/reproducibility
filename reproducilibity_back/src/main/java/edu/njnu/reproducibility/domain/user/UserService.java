package edu.njnu.reproducibility.domain.user;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.JwtUtils;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.user.dto.AddUserDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public JsonResult register( AddUserDTO add) {
        JSONObject userObj= JSONUtil.parseObj(add);
        if(userRepository.findByEmail(userObj.getStr("email")).isPresent()){
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        String userId = UUID.randomUUID().toString();
        User user = new User();
        add.convertTo(user);
        user.builder().userId(userId).build();
        return ResultUtils.success(userRepository.insert(user));
    }

    public JsonResult updateByUserId(String userId,UpdateUserDTO update) {
        User userFromDB = userRepository.findByUserId(userId).orElseThrow(MyException::noObject);
        update.updateTo(userFromDB);
        return ResultUtils.success(userRepository.save(userFromDB));
    }

    public JSONObject login(String email, String password) {
        User userFromDB = userRepository.findByEmail(email).orElseThrow(MyException::noObject);
        if(userFromDB.getPassword().equals(password)){
            String jwtToken = JwtUtils.generateToken(userFromDB.getUserId(), userFromDB.getName(), password,userFromDB.getAvatar());
            JSONObject json = new JSONObject();
            json.put("userName",userFromDB.getName());
            json.put("avatar",userFromDB.getAvatar());
            json.put("joinedProjects",userFromDB.getJoinedProjects());
            json.put("createdProjects",userFromDB.getCreatedProjects());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token","Bearer" + " " + jwtToken);
            jsonObject.put("userInfo",json);
            return jsonObject;
        }else{
            throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
        }

    }

    public JsonResult forgetPassword(String email,String password) {
        User userFromDB = userRepository.findByEmail(email).orElseThrow(MyException::noObject);
        userFromDB.builder().password(password).build();
        return ResultUtils.success(userRepository.save(userFromDB));
    }
}
