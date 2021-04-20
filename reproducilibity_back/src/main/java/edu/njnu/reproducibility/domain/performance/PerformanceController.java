package edu.njnu.reproducibility.domain.performance;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.performance.dto.AddPerformanceDTO;
import edu.njnu.reproducibility.domain.performance.dto.UpdatePerformanceDTO;
import edu.njnu.reproducibility.domain.record.RecordService;
import edu.njnu.reproducibility.domain.record.dto.AddRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:43
 * @modified By：
 * @version: 1.0.0
 */

@RestController
@RequestMapping(value = "/performances")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult getPerformanceById(@PathVariable("id") String id) {
        return  ResultUtils.success(performanceService.getPerformanceById(id));
    }

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public JsonResult getPerformanceByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(performanceService.getPerformanceByProjectId(projectId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult savePerformance(@RequestBody AddPerformanceDTO add, @JwtTokenParser(key="userId") String userId) {
        return  ResultUtils.success(performanceService.savePerformance(add,userId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public JsonResult updatePerformance(@PathVariable("id") String id,@RequestBody UpdatePerformanceDTO update, @JwtTokenParser(key="userId") String userId) {
        return  ResultUtils.success(performanceService.updatePerformance(id,update,userId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult deletePerformance(@PathVariable("id") String id){
        performanceService.deleteById(id);
        return ResultUtils.success();
    }
}
