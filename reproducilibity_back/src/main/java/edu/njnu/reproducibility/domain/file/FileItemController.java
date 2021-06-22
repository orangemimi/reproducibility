package edu.njnu.reproducibility.domain.file;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.dto.AddFileItemDTO;
import edu.njnu.reproducibility.domain.file.dto.UpdateFileItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return fileItemService.save(add,userId);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id){
        fileItemService.del(id);
    }
}
