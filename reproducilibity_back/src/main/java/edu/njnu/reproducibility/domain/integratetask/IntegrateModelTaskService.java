package edu.njnu.reproducibility.domain.integratetask;



import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.integratetask.dto.AddIntegrateModelTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateIntegratedModelTaskDTO;
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
import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:46
 * @Version 1.0.0
 */
@Service
public class IntegrateModelTaskService {
    @Autowired
    IntegrateModelTaskRepository integrateModelTaskRepository;

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Value("${wzpIpAndPort}")
    private String wzpIpAndPort;

    public JsonResult getModelTaskInfo(String id) {
        Optional<IntegrateModelTask> integrateModelTask = integrateModelTaskRepository.findFirstById(id);
        return  ResultUtils.success(integrateModelTask);
    }

    public JsonResult getAllModelTaskInfo(String pid) {
        List<IntegrateModelTask> integrateModelTask = integrateModelTaskRepository.findAllByProjectId(pid);
        return  ResultUtils.success(integrateModelTask);
    }

    public IntegrateModelTask saveModelTaskInfo(AddIntegrateModelTaskDTO add,String userId) {
//        JSONObject arr = JSONObject.parseObject(add.getModelInstances());
//        BeanUtil.copyProperties(add, target)
//        add.setModelInstanceList(arr.getJSONArray("States"));
        IntegrateModelTask integrateModelTask = new IntegrateModelTask();
        add.convertTo(integrateModelTask);
        integrateModelTask.setUserId(userId);
        return  integrateModelTaskRepository.insert(integrateModelTask);
    }

    public JsonResult updateModelTaskInfo(String id, UpdateIntegratedModelTaskDTO updateIntegratedModelTaskDTO,String userName) {
        IntegrateModelTask integrateModelTask = integrateModelTaskRepository.findFirstById(id).orElseThrow(MyException::noObject);
        updateIntegratedModelTaskDTO.updateTo(integrateModelTask);
        return ResultUtils.success(integrateModelTaskRepository.save(integrateModelTask));
    }

    public void deleteById(String id) {
        integrateModelTaskRepository.deleteById(id);
    }

    public JsonResult runTask(MultipartFile file , String taskName,String username) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + wzpIpAndPort + "/GeoModeling/task/runTask"; //模型运行

        String suffix="."+FilenameUtils.getExtension(file.getOriginalFilename());
        File temp=File.createTempFile("temp",suffix);
        file.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("userName",username);

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return ResultUtils.success(result);
    }




}
