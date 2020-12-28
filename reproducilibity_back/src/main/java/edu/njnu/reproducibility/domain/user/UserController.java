package edu.njnu.reproducibility.domain.user;

import cn.hutool.json.JSONUtil;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.user.dto.AddUserDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:28
 * @modified By：
 * @version: 1.0.0
 */

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping (value = "", method = RequestMethod.GET)
    public JsonResult getUserInfo(@JwtTokenParser(key="userId") String userId)  {
        return ResultUtils.success(userService.getUserInfo(userId));
    }


    @RequestMapping (value = "/register", method = RequestMethod.POST)
    public JsonResult doRegister(@RequestBody AddUserDTO add) {
        return ResultUtils.success(userService.register(add));
    }

    @RequestMapping (value = "/login", method = RequestMethod.POST)
    public JsonResult doLogin(@RequestParam ("email") String email,@RequestParam ("password") String password) {
        return ResultUtils.success(userService.login(email, password));
    }

    @RequestMapping (value = "", method = RequestMethod.PATCH)
    public JsonResult update( @JwtTokenParser(key="userId") String userId, @RequestBody UpdateUserDTO update) {
        return ResultUtils.success(userService.updateByUserId(userId,update));
    }

    @RequestMapping (value = "/{email}/{password}", method = RequestMethod.PATCH)
    public JsonResult forgetPassword(String email,String password) {
        return ResultUtils.success(userService.forgetPassword(email,password));
    }
}
