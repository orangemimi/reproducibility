package edu.njnu.reproducibility.domain.data;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.data.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.data.dto.UpdateDataItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @RequestMapping(value = "/steps/{stepId}",method = RequestMethod.GET)
//    public JsonResult getDataItemBystep(@PathVariable("stepId") String stepId) {
//        return dataItemService.getDataItemBystep(stepId);
//    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(dataItemService.getAll(userId)) ;
    }

    //更新部分字段
    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("id") String id,@RequestBody UpdateDataItemDTO updateDataItemDTO){
        return dataItemService.update(id,updateDataItemDTO);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult save(@RequestBody AddDataItemDTO add,@JwtTokenParser(key="userId") String userId){
        return dataItemService.save(add,userId);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id){
        dataItemService.del(id);
    }
}
