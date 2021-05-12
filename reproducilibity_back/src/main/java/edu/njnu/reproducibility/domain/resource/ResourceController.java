package edu.njnu.reproducibility.domain.resource;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceModelDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceRelatedDataDTO;
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
@RequestMapping("/resources")
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public JsonResult getResources( @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(resourceService.getResources(userId));
    }


    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public JsonResult getResourcesByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(resourceService.getResourcesByProjectId(projectId));
    }

    @RequestMapping(value = "/data/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceData(@PathVariable("pid") String pid,@RequestBody UpdateResourceDataDTO UpdateResourceDataDTO){
        return ResultUtils.success(resourceService.updateResourceData(pid, UpdateResourceDataDTO));
    }

    @RequestMapping(value = "/relatedData/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceRelatedData(@PathVariable("pid") String pid,@RequestBody UpdateResourceRelatedDataDTO UpdateResourceDataDTO){
        return ResultUtils.success(resourceService.updateResourceRelatedData(pid, UpdateResourceDataDTO));
    }

    @RequestMapping(value = "/model/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceModel(@PathVariable("pid") String pid,@RequestBody UpdateResourceModelDTO UpdateResourceModelDTO){
        return ResultUtils.success(resourceService.updateResourceModel(pid, UpdateResourceModelDTO));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveResources(@RequestBody AddResourceDTO add, @JwtTokenParser(key="userId") String userId){
        return ResultUtils.success(resourceService.saveResources(add,userId));
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(resourceService.uploadPicture(upload,userId));
    }
}
