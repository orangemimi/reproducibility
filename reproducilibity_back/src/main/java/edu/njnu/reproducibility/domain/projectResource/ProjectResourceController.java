package edu.njnu.reproducibility.domain.projectResource;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.projectResource.dto.AddProjectResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceRelatedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:20
 * @Version 1.0.0
 */
@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/projectResources")
public class ProjectResourceController {
    @Autowired
    ProjectResourceService projectResourceService;

//    @RequestMapping(value = "",method = RequestMethod.GET)
//    public JsonResult getResources( @JwtTokenParser(key="userId") String userId) {
//        return ResultUtils.success(projectResourceService.getResources(userId));
//    }


    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public JsonResult getResourcesByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(projectResourceService.getResourcesByProjectId(projectId));
    }

    @RequestMapping(value = "/data/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceData(@PathVariable("pid") String pid,@RequestBody UpdateProjectResourceDataDTO UpdateProjectResourceDataDTO){
        return ResultUtils.success(projectResourceService.updateResourceData(pid, UpdateProjectResourceDataDTO));
    }

    @RequestMapping(value = "/relatedData/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceRelatedData(@PathVariable("pid") String pid,@RequestBody UpdateProjectResourceRelatedDataDTO UpdateResourceDataDTO){
        return ResultUtils.success(projectResourceService.updateResourceRelatedData(pid, UpdateResourceDataDTO));
    }

    @RequestMapping(value = "/model/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceModel(@PathVariable("pid") String pid,@RequestBody UpdateProjectResourceModelDTO UpdateProjectResourceModelDTO){
        return ResultUtils.success(projectResourceService.updateResourceModel(pid, UpdateProjectResourceModelDTO));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveResources(@RequestBody AddProjectResourceDTO add, @JwtTokenParser(key="userId") String userId){
        return ResultUtils.success(projectResourceService.saveResources(add,userId));
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(projectResourceService.uploadPicture(upload,userId));
    }
}
