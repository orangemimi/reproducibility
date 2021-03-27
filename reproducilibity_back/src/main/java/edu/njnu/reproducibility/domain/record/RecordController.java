package edu.njnu.reproducibility.domain.record;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.record.dto.AddRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ：Zhiyi
 * @Date ：2020/11/12 20:43
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/records")
public class RecordController {
    @Autowired
    RecordService recordService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult getRecordById(@PathVariable("id") String id) {
        return  ResultUtils.success(recordService.getModelTaskInfo(id));
    }

    @RequestMapping(value = "/all/{pid}", method = RequestMethod.GET)
    public JsonResult getAllRecords(@PathVariable("pid") String pid) {
        return ResultUtils.success(recordService.getAllRecords(pid));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveRecord(@RequestBody AddRecordDTO add,@JwtTokenParser(key="userId") String userId) {
        return  ResultUtils.success(recordService.saveRecord(add,userId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteTask(@PathVariable("id") String id){
        recordService.deleteById(id);
        return ResultUtils.success();
    }


}
