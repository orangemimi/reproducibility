package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Value("${wzpIpAndPort}:8084")
    String wzpIpAndPort;

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

    @SneakyThrows
    @RequestMapping(value = "/runtask", method = RequestMethod.POST)
    JsonResult runTask(@RequestParam("file") MultipartFile file){
//       return ResultUtils.success(managerServerFeign.runtask(file, "111"));
        String userId="123";
        String suffix="."+ FilenameUtils.getExtension(file.getOriginalFilename());
        File temp= File.createTempFile("temp",suffix);
        file.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);

        RestTemplate restTemplate = new RestTemplate();
        String urlStr ="http://localhost:8084/GeoModeling/task/runTask";
//        JSONObject form = new JSONObject();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("userName",userId);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.POST, httpEntity, JSONObject.class);

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        String result = jsonObjectResponseEntity.getBody().getStr("data");

        return ResultUtils.success(result);
    }

    @RequestMapping(value = "/checkTaskStatus/{taskId}", method = RequestMethod.GET)
    JsonResult checkTaskStatus(@PathVariable("taskId") String taskId){

        String urlStr ="http://"+managerServerIpAndPort +"/GeoModeling/task/checkTaskStatus?=taskId="+taskId;
//        JSONObject form = new JSONObject();
//        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
//        param.add("taskId", taskId);
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject>  jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");


        return ResultUtils.success(result);
    }

}
