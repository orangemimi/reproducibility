package edu.njnu.reproducibility.domain.result;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.projectResource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceRelatedDataDTO;
import edu.njnu.reproducibility.domain.result.dto.AddResultCollectionDTO;
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
@RequestMapping("/results")
public class ResultController {
    @Autowired
    ResultService resultService;

    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public JsonResult getResultByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(resultService.getResultByProjectId(projectId));
    }

//    @RequestMapping(value = "/data/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
//    public JsonResult updateResourceData(@PathVariable("pid") String pid,@RequestBody UpdateResourceDataDTO UpdateResourceDataDTO){
//        return ResultUtils.success(resultService.updateResult(pid, UpdateResourceDataDTO));
//    }


    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveResult(@RequestBody AddResultCollectionDTO add, @JwtTokenParser(key="userId") String userId){
//        String userId = "444";
        return ResultUtils.success(resultService.saveResult(add,userId));
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(resultService.uploadPicture(upload,userId));
    }
}
