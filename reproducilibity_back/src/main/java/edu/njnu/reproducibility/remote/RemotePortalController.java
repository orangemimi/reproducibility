package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import feign.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static edu.njnu.reproducibility.utils.Utils.convertMdl;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/29 11:08
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/portal")
public class RemotePortalController {
    @Autowired
    RemotePortalService remotePortalService;




    @RequestMapping(value = "/modelBehavior/{id}",method = RequestMethod.GET)
    JsonResult getModel(@PathVariable(value = "id") String id){
        return ResultUtils.success(remotePortalService.getModelInfo(id));
    }

    @RequestMapping(value = "/getUnitList/{currentPage}/{pagesize}",method = RequestMethod.GET)
    JsonResult getModel( @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize){
        return ResultUtils.success(remotePortalService.getUnitList(currentPage,pagesize));
    }

    @RequestMapping(value = "/getModelList", method = RequestMethod.GET)
    JsonResult getModelList(@RequestParam int page, @RequestParam int pageSize, @RequestParam String searchText) {
        return ResultUtils.success(remotePortalService.getModelsByPortal(page, pageSize, searchText));
    }

    @RequestMapping(value = "/getComputableModels/{oid}", method = RequestMethod.GET)
    JsonResult getComputableModels(@PathVariable String oid) {
        return ResultUtils.success(remotePortalService.getComputableModels(oid));
    }
}
