package edu.njnu.reproducibility.domain.user;

import cn.hutool.json.JSONUtil;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.user.dto.AddUserDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateCreatedProjectsDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateJoinedProjectsDTO;
import edu.njnu.reproducibility.domain.user.dto.UpdateUserDTO;
import edu.njnu.reproducibility.remote.UserServiceServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        return ResultUtils.success(userService.getUserInfoById(userId));
    }

    @RequestMapping (value = "/projects", method = RequestMethod.GET)
    public JsonResult getUserProjectInfo(@JwtTokenParser(key="userId") String userId)  {
        return ResultUtils.success(userService.getUserProjectInfo(userId));
    }

    @RequestMapping (value = "/like/{email}", method = RequestMethod.GET)
    public JsonResult getUserInfoLike(@PathVariable String email)  {
        return ResultUtils.success(userService.getUserInfoLike(email));
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
    public JsonResult update( @JwtTokenParser(key="userId") String userId, @RequestBody UpdateUserDTO update, @JwtTokenParser(key = "email") String email, @JwtTokenParser(key = "password") String password) throws IllegalAccessException {
        return ResultUtils.success(userService.updateByUserId(userId, update, email, password));
    }

    @RequestMapping (value = "/join", method = RequestMethod.PATCH)
    public JsonResult updateJoinedProjects( @JwtTokenParser(key="userId") String userId, @RequestBody  String update) {
        return ResultUtils.success(userService.updateJoinedProjects(userId,update));
    }
    @RequestMapping (value = "/create", method = RequestMethod.PATCH)
    public JsonResult updateCreatedProjects(@JwtTokenParser(key="userId") String userId, @RequestBody String update) {
        return ResultUtils.success(userService.updateCreatedProjects(userId,update));
    }

    @RequestMapping (value = "/folk", method = RequestMethod.PATCH)
    public JsonResult updateFolkedProjects(@JwtTokenParser(key="userId") String userId, @RequestBody String update) {
        return ResultUtils.success(userService.updateFolkedProjects(userId,update));
    }

    //忘记密码，通过邮箱修改
    @RequestMapping (value = "/{email}/{code}/{password}", method = RequestMethod.PATCH)
    public JsonResult forgetPasswordByEmail(@PathVariable String email,@PathVariable String code, @PathVariable String password) {
        return ResultUtils.success(userService.forgetPassword(email, password, code));
    }

    //修改密码
    @RequestMapping (value = "/changePWD/{oldPWD}/{newPWD}", method = RequestMethod.PATCH)
    public JsonResult updatePassword(@PathVariable String oldPWD,@PathVariable String newPWD, @JwtTokenParser(key = "email") String email) {
        return ResultUtils.success(userService.updatePassword(oldPWD, newPWD, email));
    }

    //发送邮件，获取验证码
    @RequestMapping (value = "/sendEmail/{email}", method = RequestMethod.GET)
    public JsonResult sendCodeEmail(@PathVariable String email) {
        return ResultUtils.success(userService.sendCodeEmail(email));
    }

    //调用远程服务器，获取用户基本信息
    @RequestMapping (value = "/getuserinfo", method = RequestMethod.GET)
    public JsonResult getUserinfo(@JwtTokenParser(key = "email") String email, @JwtTokenParser(key = "password") String password) {
        return ResultUtils.success(userService.getUserinfoByUserService(email, password));
    }

    //判断用户是否star了项目
    @RequestMapping(value = "/isSart/{projectId}", method = RequestMethod.GET)
    public JsonResult isStarProject(@JwtTokenParser(key = "userId") String userId, @PathVariable String projectId) {
        return ResultUtils.success(userService.isSartedProject(userId, projectId));
    }
}
