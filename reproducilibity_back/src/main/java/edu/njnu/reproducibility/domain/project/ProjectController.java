package edu.njnu.reproducibility.domain.project;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectMembersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:44
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/one/{projectId}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("projectId") String projectId,@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.get(projectId));
    }

    @RequestMapping(value = "/getmyprojects", method = RequestMethod.GET)
    public JsonResult getMyProjects(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.getMyProjects(userId));
    }

    @RequestMapping(value = "/user/{projectId}", method = RequestMethod.GET)
    public JsonResult getProjectAndUsers(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(projectService.getProjectAndUsers(projectId));
    }

    @RequestMapping(value = "/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getAllProjects(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(projectService.getAllProjects(userId, currentPage, pagesize));
    }

//    @RequestMapping(value = "/createdProjects", method = RequestMethod.GET)
//    public JsonResult getCreatedProjectsByUser(@JwtTokenParser(key = "userId") String userId) {
//        return ResultUtils.success(projectService.getCreatedProjectsByUser(userId));
//    }

    @RequestMapping(value = "/members/{projectId}", method = RequestMethod.PATCH)
    public JsonResult updateMembers(@JwtTokenParser(key = "userId") String userId,
                                    @PathVariable("projectId") String projectId,
                                    @RequestBody Member upate) {
        return ResultUtils.success(projectService.updateMembers(userId, projectId, upate));
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("projectId") String projectId,
                             @JwtTokenParser(key = "userId") String userId,
                             @RequestBody UpdateProjectDTO update) {
        return ResultUtils.success(projectService.update(projectId, userId, update));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult create(@JwtTokenParser(key = "userId") String userId, @JwtTokenParser(key = "name") String name, @RequestBody AddProjectDTO add) {
        return ResultUtils.success(projectService.create(userId, name, add));
    }

    @RequestMapping(value = "/fork", method = RequestMethod.POST)
    public JsonResult folk(@JwtTokenParser(key = "userId") String userId, @JwtTokenParser(key = "name") String name, @RequestBody AddProjectDTO add) {
        return ResultUtils.success(projectService.fork(userId, name, add));
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("projectId") String projectId) {
        projectService.delete(projectId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key = "userId") String userId) {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(projectService.uploadPicture(upload, userId));
    }

    @RequestMapping(value = "/picture/{fileName}", method = RequestMethod.DELETE)
    public JsonResult deletePicture(@PathVariable("fileName") String fileName, @JwtTokenParser(key = "userId") String userId) {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(projectService.deletePicture(fileName, userId));
    }

    @RequestMapping(value = "/star/{projectId}", method = RequestMethod.PATCH)
    public JsonResult starProject(@PathVariable("projectId") String projectId, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.Star(projectId, userId));
    }

    @RequestMapping(value = "/unStar/{projectId}", method = RequestMethod.PATCH)
    public JsonResult unStarProject(@PathVariable String projectId, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.unStar(projectId, userId));
    }

    @RequestMapping(value = "/getStarCount/{projectId}", method = RequestMethod.GET)
    public JsonResult getStarCount(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getStarCount(projectId));
    }

    @RequestMapping(value = "/getPrivacy/{projectId}", method = RequestMethod.GET)
    public JsonResult getPrivacy(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getPrivacyOfProject(projectId));
    }



}
