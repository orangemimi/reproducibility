package edu.njnu.reproducibility.domain.resource.model;



import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.model.dto.AddModelItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 11:27
 * @modified By：
 * @version:     1.0.0
 */
@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/modelItems")
public class ModelItemController {
    @Autowired
    ModelItemService modelItemService;

//    @RequestMapping(value = "/{pid}",method = RequestMethod.GET)
//    public JsonResult getAll(@PathVariable("pid") String pid) {
//        return modelItemService.getAll(pid);
//    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(modelItemService.getAll(userId));
    }

    //更新部分字段
//    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
//    public JsonResult update(@PathVariable("id") String id,@RequestBody UpdateDataItemDTO updateDataItemDTO){
//        return dataItemService.update(id,updateDataItemDTO);
//    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult save(@RequestBody AddModelItemDTO add,@JwtTokenParser(key="userId") String userId){
        return modelItemService.save(add,userId);
    }

//    @RequestMapping(value = "/{tid}",method = RequestMethod.DELETE)
//    public JsonResult delete(@PathVariable("tid") String tid){
//        modelItemService.del(tid);
//        return ResultUtils.success();
//    }
}
