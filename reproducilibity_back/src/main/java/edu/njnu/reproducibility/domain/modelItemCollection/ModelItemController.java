package edu.njnu.reproducibility.domain.modelItemCollection;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.modelItemCollection.dto.AddModelItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2020/10/14 11:27
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/modelItems")
public class ModelItemController {
    @Autowired
    ModelItemService modelItemService;

//    @RequestMapping(value = "/{pid}",method = RequestMethod.GET)
//    public JsonResult getAll(@PathVariable("pid") String pid) {
//        return modelItemService.getAll(pid);
//    }

    @RequestMapping(value = "/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(modelItemService.getAll(userId, currentPage, pagesize));
    }

    @RequestMapping(value = "/{privacy}/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getPublicModels(@PathVariable("privacy") String privacy, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(modelItemService.getPublicModels(privacy, currentPage, pagesize));
    }

    @RequestMapping(value = "/provider/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getModelsByProviderAndPrivacy(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(modelItemService.getModelsByProvider(userId, currentPage, pagesize));
    }


    //更新部分字段
//    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
//    public JsonResult update(@PathVariable("id") String id,@RequestBody UpdateDataItemDTO updateDataItemDTO){
//        return dataItemService.update(id,updateDataItemDTO);
//    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult save(@RequestBody AddModelItemDTO add, @JwtTokenParser(key = "userId") String userId) {
        return modelItemService.save(add, userId);
    }

//    @RequestMapping(value = "/{tid}",method = RequestMethod.DELETE)
//    public JsonResult delete(@PathVariable("tid") String tid){
//        modelItemService.del(tid);
//        return ResultUtils.success();
//    }

    @RequestMapping(value = "/saveToProject", method = RequestMethod.POST)
    public JsonResult saveToProject(@RequestBody JSONObject jsonObject, @JwtTokenParser String userId) {
        modelItemService.saveToProject(jsonObject, userId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/saveModelsToProject", method = RequestMethod.POST)
    public JsonResult saveModelsToProject(@RequestBody JSONObject jsonObject, @JwtTokenParser String userId) {
        modelItemService.saveModelsToProject(jsonObject.getJSONArray("models"), jsonObject.getStr("projectId"), userId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getModelsByProjectId/{projectId}", method = RequestMethod.GET)
    public JsonResult getModelsByProjectId(@PathVariable String projectId) {
        return ResultUtils.success(modelItemService.getModelsByProjectId(projectId));
    }

    @RequestMapping(value = "/getPictureByDOI/{Doi}", method = RequestMethod.GET)
    public JsonResult getPictureByDOI(@PathVariable String Doi) {
        return ResultUtils.success(modelItemService.getPictureByDOI(Doi));
    }
}
