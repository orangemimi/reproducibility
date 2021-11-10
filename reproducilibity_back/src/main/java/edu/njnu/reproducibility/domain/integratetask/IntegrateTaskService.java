package edu.njnu.reproducibility.domain.integratetask;


import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.integratetask.dto.AddIntegrateTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateCurrentActionInTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateIntegratedTaskDTO;
import edu.njnu.reproducibility.domain.scenario.Scenario;
import edu.njnu.reproducibility.domain.scenario.ScenarioRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:46
 * @Version 1.0.0
 */
@Service
public class IntegrateTaskService {
    @Autowired
    IntegrateTaskRepository integrateTaskRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Value("${wzpIpAndPort}")
    private String wzpIpAndPort;

    public IntegrateTask getModelTaskInfo(String id) {
        IntegrateTask integrateModelTask = integrateTaskRepository.findFirstById(id).orElseThrow(MyException::noObject);
        return integrateModelTask;
    }


    public List<IntegrateTask> getAllModelTaskInfo(String pid) {
        List<IntegrateTask> integrateTask = integrateTaskRepository.findAllByProjectId(pid);
        return integrateTask;
    }

    public IntegrateTask saveModelTaskInfo(AddIntegrateTaskDTO add, String userId) {
//        JSONObject arr = JSONObject.parseObject(add.getModelInstances());
//        BeanUtil.copyProperties(add, target)
//        add.setModelInstanceList(arr.getJSONArray("States"));
        IntegrateTask integrateTask = new IntegrateTask();
        add.convertTo(integrateTask);
        integrateTask.setCreator(userId);
        return integrateTaskRepository.insert(integrateTask);
    }

    public IntegrateTask updateModelTaskInfo(String id, UpdateIntegratedTaskDTO updateIntegratedTaskDTO, String userName) {
        IntegrateTask integrateTask = integrateTaskRepository.findFirstById(id).orElseThrow(MyException::noObject);
        updateIntegratedTaskDTO.updateTo(integrateTask);
        return integrateTaskRepository.save(integrateTask);
    }

    public IntegrateTask updateSelectAction(String id, UpdateCurrentActionInTaskDTO update, String userId) {
        IntegrateTask integrateTask = integrateTaskRepository.findFirstById(id).orElseThrow(MyException::noObject);
        update.updateTo(integrateTask);
        return integrateTaskRepository.save(integrateTask);
    }

    public IntegrateTask changeSelectInstance(String id, String instanceId, String type) {
        IntegrateTask integrateTask = integrateTaskRepository.findFirstById(id).orElseThrow(MyException::noObject);
        if(type.equals("star")) {
            List<String> temp = integrateTask.getSelectInstanceId();
            if(temp == null) {
                integrateTask.setSelectInstanceId(new ArrayList<>());
            }
            integrateTask.selectInstanceId.add(instanceId);
        } else if(type.equals("unstar")) {
            List<String> temp = integrateTask.getSelectInstanceId();
            if(temp == null) {
                throw new MyException(ResultEnum.NO_OBJECT);
            }
            integrateTask.selectInstanceId.remove(instanceId);
        }
        return integrateTaskRepository.save(integrateTask);
    }

    public void deleteById(String id) {
        integrateTaskRepository.deleteById(id);
    }

    public JSONObject runTask(MultipartFile file, String taskName, String username) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + wzpIpAndPort + "/GeoModeling/task/runTask"; //模型运行

        String suffix = "." + FilenameUtils.getExtension(file.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        file.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("userName", username);

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public IntegrateTask getSelectedTaskByProjectId(String projectId) {
        Scenario scenario = scenarioRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        String taskId = scenario.getSelectTaskId();
        return integrateTaskRepository.findById(taskId).orElseThrow(MyException::noObject);
    }

}
