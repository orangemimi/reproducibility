package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static edu.njnu.reproducibility.utils.Utils.convertMdl;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/29 11:08
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/portal")
public class RemotePortalFeignController {
    @Autowired
    PortalFeign portalFeign;

    @RequestMapping(value = "/modelBehaviorOrdinary/{id}",method = RequestMethod.GET)
    JsonResult getModelOrdinary(@PathVariable String id){
        return ResultUtils.success(portalFeign.getComputeModel(id));
    }

    @RequestMapping(value = "/modelBehavior/{id}",method = RequestMethod.GET)
    JsonResult getModel(@PathVariable String id){
       JSONObject result= portalFeign.getComputeModel(id);
        return ResultUtils.success((result));
    }
}
