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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
public class RemotePortalFeignController {

    @RequestMapping(value = "/modelBehaviorOrdinary/{id}",method = RequestMethod.GET)
    JsonResult getModelOrdinary(@PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://geomodeling.njnu.edu.cn/computableModel/getInfo/" + id; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }

        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return ResultUtils.success(result);
    }

    @RequestMapping(value = "/modelBehavior/{id}",method = RequestMethod.GET)
    JsonResult getModel(@PathVariable(value = "id") String id){
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://222.192.7.75:80/computableModel/getInfo/" + id; ////Step0:根据MD5获取可用的任务服务器
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.GET,httpEntity, JSONObject.class);//虚拟http请求

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }

        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");

        return ResultUtils.success(convertMdl(result));
    }
}
