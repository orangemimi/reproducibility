package edu.njnu.reproducibility.domain.dataItemCollection;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.UpdateDataItemDTO;
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
@RequestMapping("/dataItems")
public class DataItemController {
    @Autowired
    DataItemService dataItemService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public JsonResult getResources( @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(dataItemService.getResources(userId));
    }


    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public JsonResult getResourcesByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(dataItemService.getResourcesByProjectId(projectId));
    }

    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult updateResourceData(@PathVariable("id") String id,@RequestBody UpdateDataItemDTO update){
        return ResultUtils.success(dataItemService.updateResourceData(id, update));
    }
//
//    @RequestMapping(value = "/relatedData/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
//    public JsonResult updateResourceRelatedData(@PathVariable("pid") String pid,@RequestBody UpdateResourceRelatedDataDTO UpdateResourceDataDTO){
//        return ResultUtils.success(dataItemService.updateResourceRelatedData(pid, UpdateResourceDataDTO));
//    }
//
//    @RequestMapping(value = "/model/{pid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
//    public JsonResult updateResourceModel(@PathVariable("pid") String pid,@RequestBody UpdateResourceModelDTO UpdateResourceModelDTO){
//        return ResultUtils.success(dataItemService.updateResourceModel(pid, UpdateResourceModelDTO));
//    }


    

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveResources(@RequestBody AddDataItemDTO add, @JwtTokenParser(key="userId") String userId){
        return ResultUtils.success(dataItemService.saveResources(add,userId));
    }

    @RequestMapping (value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(@RequestParam("pictureFile") MultipartFile upload, @JwtTokenParser(key="userId") String userId)  {
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile upload = multiRequest.getFile("pictureFile");
        return ResultUtils.success(dataItemService.uploadPicture(upload,userId));
    }

    @RequestMapping(value = "/del/{Id}", method = RequestMethod.DELETE)
    public JsonResult delDataItem(@PathVariable String Id) {
        return ResultUtils.success(dataItemService.delDataItem(Id));
    }
}
