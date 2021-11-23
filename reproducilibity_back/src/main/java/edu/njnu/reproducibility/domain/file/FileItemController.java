package edu.njnu.reproducibility.domain.file;


import com.alibaba.fastjson.JSONObject;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.dto.AddFileItemDTO;
import edu.njnu.reproducibility.domain.file.dto.UpdateFileItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:20
 * @Version 1.0.0
 */
@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/fileItems")
public class FileItemController {
    @Autowired
    FileItemService fileItemService;

//    @RequestMapping(value = "/steps/{stepId}",method = RequestMethod.GET)
//    public JsonResult getDataItemBystep(@PathVariable("stepId") String stepId) {
//        return dataItemService.getDataItemBystep(stepId);
//    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(fileItemService.getAll(userId)) ;
    }


    @RequestMapping(value = "/creator/{projectId}",method = RequestMethod.GET)
    public JsonResult getResourcesByCreatorId( @JwtTokenParser(key="userId") String userId,@PathVariable("projectId") String projectId) {
        return ResultUtils.success(fileItemService.getByCreatorId(userId,projectId));
    }

    //更新部分字段
    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("id") String id,@RequestBody UpdateFileItemDTO updateFileItemDTO){
        return fileItemService.update(id, updateFileItemDTO);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult save(@RequestBody AddFileItemDTO add, @JwtTokenParser(key="userId") String userId){
        return ResultUtils.success(fileItemService.save(add, userId));
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id){
        fileItemService.del(id);
    }

    @RequestMapping(value = "/getFileItemByStoreyAndParent/{storey}/{parent}", method = RequestMethod.GET)
    public JsonResult getFileItemByStoreyAndParent(@PathVariable String storey, @PathVariable String parent, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(fileItemService.getFileItemByStoreyAndParent(parent, storey, userId));
    }

    @RequestMapping(value = "/Rename", method = RequestMethod.PATCH)
    public JsonResult Rename(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        return ResultUtils.success(fileItemService.Rename(id, name));
    }

    @RequestMapping(value = "/addFileItem", method = RequestMethod.POST)
    public JsonResult addFileItem(HttpServletRequest httpServletRequest, @JwtTokenParser(key = "userId") String userId) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile =  multipartHttpServletRequest.getFile("datafile");
        String name = multipartHttpServletRequest.getParameter("name");
        String parent = multipartHttpServletRequest.getParameter("parent");
        String storey = multipartHttpServletRequest.getParameter("storey");
        return ResultUtils.success(fileItemService.addFileItem(name, userId, multipartFile, parent, storey));
    }

    @RequestMapping(value = "/delFile/{storey}/{id}/{parent}", method = RequestMethod.DELETE)
    public JsonResult delFile(@PathVariable String storey, @PathVariable String id, @PathVariable String parent) {
        fileItemService.delFile(storey, id, parent);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/delFolder/{storey}/{id}/{parent}", method = RequestMethod.DELETE)
    public JsonResult delFolder(@PathVariable String storey, @PathVariable String id, @PathVariable String parent) {
        fileItemService.delFolder(storey, id, parent);
        return ResultUtils.success();
    }
}
