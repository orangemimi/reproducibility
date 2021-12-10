package edu.njnu.reproducibility.domain.integratetaskInstance;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTaskRepository;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.AddIntegrateTaskInstanceDTO;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.UpdateIntegrateTaskInstanceDTO;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.UpdateNoteTaskInstanceDTO;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.TaskInfo;
import edu.njnu.reproducibility.domain.scenario.Scenario;
import edu.njnu.reproducibility.domain.scenario.ScenarioRepository;
import edu.njnu.reproducibility.domain.scenario.ScenarioService;
import edu.njnu.reproducibility.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 16:41
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class IntegrateTaskInstanceService {
    @Autowired
    IntegrateTaskInstanceRepository integrateTaskInstanceRepository;

    @Autowired
    ScenarioService scenarioService;

    @Autowired
    IntegrateTaskRepository integrateTaskRepository;

    @Autowired
    UserService userService;


    public IntegrateTaskInstance getById(String id) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(id).orElseThrow(MyException::noObject);
        return integrateTaskInstance;
    }


    public Page<IntegrateTaskInstance>  getAllByTaskId(String taskId, String userId,int currentPage, int pagesize ) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
        Sort sort = Sort.by(orderList);
        PageRequest pageable =  PageRequest.of(currentPage, pagesize, sort);
        Page<IntegrateTaskInstance> actionList= integrateTaskInstanceRepository.findAllByTaskIdAndOperatorId(taskId,userId,pageable);
        return actionList;
    }

    public Page<IntegrateTaskInstance>  getAllByOperatorId(String userId, int currentPage, int pagesize ) {
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        Page<IntegrateTaskInstance> actionList= integrateTaskInstanceRepository.findAllByOperatorId(userId,pageable);
        return actionList;
    }

    public IntegrateTaskInstance create(String userId, AddIntegrateTaskInstanceDTO add) {
        IntegrateTaskInstance integrateTaskInstance = new IntegrateTaskInstance();
        add.convertTo(integrateTaskInstance);
        integrateTaskInstance.setOperatorId(userId);
        return integrateTaskInstanceRepository.insert(integrateTaskInstance);
    }

    public void delete(String actionId) {
        integrateTaskInstanceRepository.deleteById(actionId);
    }

    public Object updateById(String id, UpdateIntegrateTaskInstanceDTO update, String userId) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(id).orElseThrow(MyException::noObject);
        update.updateTo(integrateTaskInstance);
        return integrateTaskInstanceRepository.save(integrateTaskInstance);
    }

    public Object updateByNoteId(String id, UpdateNoteTaskInstanceDTO update, String userId) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(id).orElseThrow(MyException::noObject);
        update.updateTo(integrateTaskInstance);
        return integrateTaskInstanceRepository.save(integrateTaskInstance);
    }

    public List<String> getInstanceOfUncompleted(String userId, String taskId) {
        List<IntegrateTaskInstance> integrateTaskInstances = integrateTaskInstanceRepository.findAllByTaskIdAndOperatorId(taskId, userId);
        List<String> tides = new ArrayList<>();
        for(IntegrateTaskInstance temp : integrateTaskInstances) {
            if(temp.getStatus() != 1) {
                tides.add(temp.tid);
            }
        }
        return tides;
    }

    public String getXML(String instanceId) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(instanceId).orElseThrow(MyException::noObject);
        return integrateTaskInstance.getTaskContent();
    }

    public TaskInfo getTaskInfo(String instanceId) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(instanceId).orElseThrow(MyException::noObject);
        return integrateTaskInstance.getTaskInfo();
    }

    public JSONObject getAllIntegrateTaskInstance(String taskId, String userId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Page<IntegrateTaskInstance> pages = integrateTaskInstanceRepository.findAllByTaskIdAndOperatorId(taskId, userId, PageRequest.of(page, size, sort));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", pages.getContent());
        jsonObject.put("total", integrateTaskInstanceRepository.countByTaskIdAndOperatorId(taskId, userId));
        return jsonObject;
    }

    public IntegrateTaskInstance getSelectedInstancesByProjectId(String projectId) {
        Scenario scenario = scenarioService.getScenario(projectId).orElseThrow(MyException::noObject);
        String taskId = scenario.getSelectTaskId();
        IntegrateTask integrateTask = integrateTaskRepository.findById(taskId).orElseThrow(MyException::noObject);
        String selectedId = integrateTask.getSelectInstanceId();
        return integrateTaskInstanceRepository.findById(selectedId).orElseThrow(MyException::noObject);
    }

    public Map<String, List<com.alibaba.fastjson.JSONObject>> getAllInstancesOfReproductionByProjectId(String projectId) {
        Scenario scenario = scenarioService.getScenario(projectId).orElseThrow(MyException::noObject);
        String userId = scenario.getUserId();
        String taskId = scenario.getSelectTaskId();
        List<IntegrateTaskInstance> allByTaskId = integrateTaskInstanceRepository.findAllByTaskId(taskId, userId);
        Map<String, List<com.alibaba.fastjson.JSONObject>> result = new HashMap<>();
        List<com.alibaba.fastjson.JSONObject> pub = new ArrayList<>();
        List<com.alibaba.fastjson.JSONObject> pri = new ArrayList<>();
        for(IntegrateTaskInstance temp : allByTaskId) {
            String json = com.alibaba.fastjson.JSONObject.toJSONString(temp);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);
            Map<String, Object> userInfoByUserId = userService.getUserInfoByUserId(temp.getOperatorId());
            jsonObject.put("userInfo", userInfoByUserId);
            if(temp.getAuthority().equals("public")) {
                pub.add(jsonObject);
            } else {
                pri.add(jsonObject);
            }
        }
        result.put("public", pub);
        result.put("private", pri);
        return result;
    }
}
