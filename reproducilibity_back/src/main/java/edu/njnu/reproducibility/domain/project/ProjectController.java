package edu.njnu.reproducibility.domain.project;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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

    @RequestMapping (value = "/one/{projectId}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(projectService.get(projectId));
    }

    @RequestMapping (value = "", method = RequestMethod.GET)
    public JsonResult getAllProjects() {
        return ResultUtils.success(projectService.getAllProjects());
    }
    @RequestMapping (value = "/createdProjects", method = RequestMethod.GET)
    public JsonResult getCreatedProjectsByUser(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.getCreatedProjectsByUser(userId));
    }
    @RequestMapping (value = "/joinedProjects", method = RequestMethod.GET)
    public JsonResult getJoinedProjectsByUser(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(projectService.getJoinedProjectsByUser(userId));
    }

    @RequestMapping (value = "/{projectId}", method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("projectId") String projectId,
                             @JwtTokenParser(key="userId") String userId,
                             @RequestBody UpdateProjectDTO update) {
        return ResultUtils.success(projectService.update(projectId,userId,update));
    }

    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult create(@JwtTokenParser(key="userId") String userId, @RequestBody AddProjectDTO add) {
        return ResultUtils.success(projectService.create(userId,add));
    }

    @RequestMapping (value = "/{projectId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("projectId") String projectId) {
        projectService.delete(projectId);
        return ResultUtils.success();
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload,@JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(projectService.uploadPicture(upload,userId));
    }
    @RequestMapping (value = "/picture/{fileName}", method = RequestMethod.DELETE)
    public JsonResult deletePicture(@PathVariable("fileName") String fileName,@JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(projectService.deletePicture(fileName,userId));
    }

}
