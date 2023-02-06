package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.FileItem;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstance;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstanceRepository;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.Process;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.TaskInfo;
import edu.njnu.reproducibility.utils.Utils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    IntegrateTaskInstanceRepository integrateTaskInstanceRepository;

    @Autowired
    FileItemRepository fileItemRepository;

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
    JsonResult runTask(@RequestParam("file") MultipartFile file,@JwtTokenParser(key="name") String name){
//       return ResultUtils.success(managerServerFeign.runtask(file, "111"));
//        String userId="123";
        String suffix="."+ FilenameUtils.getExtension(file.getOriginalFilename());
        File temp= File.createTempFile("temp",suffix);
        file.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);

        RestTemplate restTemplate = new RestTemplate();
        String urlStr ="http://172.21.213.245:8084/GeoModeling/task/runTask";
//        JSONObject form = new JSONObject();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("userName",name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

        try {
            ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.POST, httpEntity, JSONObject.class);
            if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
                throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
            }
            String result = jsonObjectResponseEntity.getBody().getStr("data");
            return ResultUtils.success(result);
        }catch (Exception e) {
            System.out.println(e);
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
    }

    @RequestMapping(value = "/checkTaskStatus/{tid}", method = RequestMethod.GET)
    JsonResult checkTaskStatus(@PathVariable("tid") String tid, @JwtTokenParser(key = "userId") String userId){

//        String urlStr ="http://"+managerServerIpAndPort +"/GeoModeling/task/checkTaskStatus?taskId="+taskId;
        String urlStr ="http://172.21.213.245:8084/GeoModeling/task/checkTaskStatus?taskId="+tid;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject>  jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findByTid(tid).orElseThrow(MyException::noObject);
        TaskInfo taskInfo = result.getJSONObject("taskInfo").toBean(TaskInfo.class);

//        JSONArray modelCompleted = result.getJSONObject("taskInfo").getJSONObject("modelActionList").getJSONArray("completed");
//        for(int i = 0; i < modelCompleted.size(); i++) {
//            List<String> temp = new ArrayList<>();
//            for(Process p : integrateTaskInstance.getTaskInfo().getModelActionList().getCompleted()) {
//                temp.add(p.getId());
//            }
//            if(!temp.contains(modelCompleted.get(i, JSONObject.class).getStr("id"))) {
//                JSONArray outputs = ((JSONObject) modelCompleted.get(i)).getJSONObject("outputData").getJSONArray("outputs");
//                for(int j = 0;j < outputs.size();j++) {
//                    JSONObject dataContent = ((JSONObject) outputs.get(j)).getJSONObject("dataContent");
//                    FileItem fileItem = new FileItem();
//                    fileItem.setAddress(dataContent.getStr("value"));
//                    fileItem.setUserUpload(dataContent.getStr("type").equals("url"));
//                    fileItem.setUploaderId(userId);
//                    fileItem.setName(dataContent.getStr("fileName"));
//                    fileItem.setSuffix(dataContent.getStr("suffix"));
//                    fileItemRepository.insert(fileItem);
//                }
//            }
//        }
//        JSONArray dataServiceCompleted = result.getJSONObject("taskInfo").getJSONObject("dataProcessingList").getJSONArray("completed");
//        for(int i = 0; i < dataServiceCompleted.size(); i++) {
//            List<String> temp = new ArrayList<>();
//            for(Process p : integrateTaskInstance.getTaskInfo().getDataProcessingList().getCompleted()) {
//                temp.add(p.getId());
//            }
//            if(!temp.contains(dataServiceCompleted.get(i, JSONObject.class).getStr("id"))) {
//                JSONArray outputs = ((JSONObject) dataServiceCompleted.get(i)).getJSONObject("outputData").getJSONArray("outputs");
//                for(int j = 0;j < outputs.size();j++) {
//                    JSONObject dataContent = ((JSONObject) outputs.get(j)).getJSONObject("dataContent");
//                    FileItem fileItem = new FileItem();
//                    fileItem.setAddress(dataContent.getStr("value"));
//                    fileItem.setUserUpload(dataContent.getStr("type").equals("url"));
//                    fileItem.setUploaderId(userId);
//                    fileItem.setName(dataContent.getStr("fileName"));
//                    fileItem.setSuffix(dataContent.getStr("suffix"));
//                    fileItemRepository.insert(fileItem);
//                }
//            }
//        }

        if(!taskInfo.toString().equals(integrateTaskInstance.getTaskInfo().toString())) {
            integrateTaskInstance.setTaskInfo(taskInfo);
            if(result.getInt("status") == 1) {
                integrateTaskInstance.setStatus(1);
            }
            integrateTaskInstanceRepository.save(integrateTaskInstance);
        }



//        List<Map<String, String>> completedValue = Utils.getValueList(result, "completed");
//        List<Map<String, String>> failedValue = Utils.getValueList(result, "failed");
//
//        Utils.changeStatusOfCell(integrateTaskInstance, completedValue, failedValue);



        return ResultUtils.success(integrateTaskInstance);
    }

}
