package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/29 10:56
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/managerServer")
public class RemoteManagerServerController {
    @Autowired
    ManagerServerFeign managerServerFeign;

    @RequestMapping(value = "/getServiceTask/{pid}", method = RequestMethod.GET)
    JsonResult getServiceTask(@PathVariable String pid){
        return ResultUtils.success(managerServerFeign.getServiceTask(pid));
    }

    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    JsonResult createTask(@RequestBody JSONObject serviceJson){
//        ip port pid username
        return ResultUtils.success(managerServerFeign.createTask(serviceJson));
    }

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    JsonResult getServiceTask(@RequestBody JSONObject obj){
        return ResultUtils.success(managerServerFeign.invoke(obj));
    }
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    JsonResult refresh(@RequestBody JSONObject obj){
        return ResultUtils.success(managerServerFeign.refresh(obj));
    }
}
