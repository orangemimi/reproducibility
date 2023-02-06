package edu.njnu.reproducibility.domain.integratetask;


import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.integratetask.dto.AddIntegrateTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateCurrentActionInTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateIntegratedTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.support.Process;
import edu.njnu.reproducibility.domain.integratetask.support.Result;
import edu.njnu.reproducibility.domain.integratetask.support.ResultValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:44
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/integrateTasks")
public class IntegrateTaskController {
    @Autowired
    IntegrateTaskService integrateTaskService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult getTask(@PathVariable("id") String id, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskService.getModelTaskInfo(id));
    }


    @RequestMapping(value = "/all/{pid}", method = RequestMethod.GET)
    public JsonResult getAllTask(@PathVariable("pid") String pid) {
        return ResultUtils.success(integrateTaskService.getAllModelTaskInfo(pid));
    }

    //model action数据配置引发更新，其他的均可选另存为一个task，或更新
    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateTask(@PathVariable("id") String id, @RequestBody UpdateIntegratedTaskDTO UpdateResourceDTO, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskService.updateModelTaskInfo(id, UpdateResourceDTO, userId));
    }


    @RequestMapping(value = "/changeSelectInstance", method = RequestMethod.PATCH)
    public JsonResult changeSelectInstance(@RequestBody JSONObject form) {
        return ResultUtils.success(integrateTaskService.changeSelectInstance(form.getStr("id"), form.getStr("instanceId")));
    }
//    @RequestMapping(value = "/changeSelectInstance", method = RequestMethod.GET)
//    public JsonResult changeSelectInstance() {
//        return ResultUtils.success("haha");
//    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveTask(@RequestBody AddIntegrateTaskDTO add, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskService.saveModelTaskInfo(add, userId));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteTask(@PathVariable("id") String id) {
        integrateTaskService.deleteById(id);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/run", method = RequestMethod.POST)
    public JsonResult runTask(@RequestParam("file") MultipartFile file,
                              @RequestParam("taskName") String taskName,
                              HttpServletRequest request) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            return ResultUtils.error(-1, "no login");
        } else {
            String username = session.getAttribute("userName").toString();
            return ResultUtils.success(integrateTaskService.runTask(file, taskName, username));
        }
    }

    @RequestMapping(value = "/getSelectedTaskByProjectId/{projectId}", method = RequestMethod.GET)
    public JsonResult getSelectedTaskByProjectId(@PathVariable String projectId) {
        return ResultUtils.success(integrateTaskService.getSelectedTaskByProjectId(projectId));
    }

    @RequestMapping(value = "/addProcess/{id}", method = RequestMethod.PATCH)
    public JsonResult addProcess(@PathVariable String id,@RequestBody Process process) {
        integrateTaskService.addProcess(id,process);
        return ResultUtils.success();
    }
    @RequestMapping(value = "/editProcess/{id}", method = RequestMethod.PATCH)
    public JsonResult editProcess(@PathVariable String id,@RequestBody JSONObject jsonObject) {
        Process process = jsonObject.get("form",Process.class);
        Integer index = jsonObject.getInt("index");
        integrateTaskService.editProcess(id,process,index);
        return ResultUtils.success();
    }
    @RequestMapping(value = "/deleteProcess/{id}", method = RequestMethod.PATCH)
    public JsonResult deleteProcess(@PathVariable String id,@RequestBody JSONObject jsonObject) {
        Integer index = jsonObject.getInt("index");
        integrateTaskService.deleteProcess(id,index);
        return ResultUtils.success();
    }
    @RequestMapping(value = "/getIntegrateTaskResultByTaskId/{id}", method = RequestMethod.GET)
    public JsonResult getIntegrateTaskResultByTaskId(@PathVariable String id) {
        return ResultUtils.success(integrateTaskService.getIntegrateTaskResultByTaskId(id));
    }
    @RequestMapping(value = "/saveResult/{id}",method = RequestMethod.PATCH)
    public JsonResult saveResult(@RequestBody Result result, @PathVariable String id){
        integrateTaskService.saveResult(id, result);
        return ResultUtils.success();
    }
    @RequestMapping(value = "/deleteResultValidation/{id}",method = RequestMethod.PATCH)
    public JsonResult deleteResultValidation(@RequestBody ResultValidation resultValidation, @PathVariable String id){
        integrateTaskService.deleteResultValidation(id, resultValidation);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/saveResultValidation/{id}", method = RequestMethod.PATCH)
    public JsonResult saveResultValidation(@RequestBody ResultValidation resultValidation, @PathVariable String id) {
        integrateTaskService.saveResultValidation(id, resultValidation);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/updateResultValidation/{id}", method = RequestMethod.PATCH)
    public JsonResult updateResultValidation(@RequestBody com.alibaba.fastjson.JSONObject jsonObject, @PathVariable String id) {
        ResultValidation resultValidation = jsonObject.getObject("data",ResultValidation.class);
        Integer index = jsonObject.getInteger("index");
        integrateTaskService.updateResultValidation(id, index, resultValidation);
        return ResultUtils.success();
    }
}
