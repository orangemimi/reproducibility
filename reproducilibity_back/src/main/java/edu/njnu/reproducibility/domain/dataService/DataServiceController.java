package edu.njnu.reproducibility.domain.dataService;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataService.dto.AddDataServiceDTO;
import edu.njnu.reproducibility.domain.result.dto.AddResultCollectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/dataServices")
public class DataServiceController {
    @Autowired
    DataServiceService dataServiceService;

    @RequestMapping(value = "/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(dataServiceService.getAll(userId, currentPage, pagesize));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveService(@JwtTokenParser(key = "userId") String userId,@RequestBody AddDataServiceDTO add) {
        return ResultUtils.success(dataServiceService.save(userId,add));
    }

    @RequestMapping(value = "/getmydataservices", method = RequestMethod.GET)
    public JsonResult getMyDataServices(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(dataServiceService.getMyDataService(userId));
    }

    @RequestMapping(value = "/saveDataServicesToProject", method = RequestMethod.POST)
    public JsonResult saveDataServicesToProject(@RequestBody JSONObject jsonObject, @JwtTokenParser String userId) {
        dataServiceService.saveDataServicesToProject(jsonObject.getJSONArray("dataServices"), jsonObject.getStr("projectId"), userId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getAllByProjectId/{projectId}", method = RequestMethod.GET)
    public JsonResult getAllByProjectId(@PathVariable String projectId) {
        return ResultUtils.success(dataServiceService.getAllByProjectId(projectId));
    }
}
