package edu.njnu.reproducibility.domain.resource;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceDTO;
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

    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public JsonResult getResources(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(resourceService.getResources(projectId));
    }

    @RequestMapping(value = "/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResources(@PathVariable("pid") String pid,@RequestBody UpdateResourceDTO UpdateResourceDTO){
        return ResultUtils.success(resourceService.updateResources(pid,UpdateResourceDTO));
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public JsonResult saveResources(@RequestBody AddResourceDTO add){
        return ResultUtils.success(resourceService.saveResources(add));
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(resourceService.uploadPicture(upload,userId));
    }
}
