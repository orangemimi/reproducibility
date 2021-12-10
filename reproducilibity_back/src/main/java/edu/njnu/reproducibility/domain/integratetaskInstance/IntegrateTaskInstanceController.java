package edu.njnu.reproducibility.domain.integratetaskInstance;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.AddIntegrateTaskInstanceDTO;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.UpdateIntegrateTaskInstanceDTO;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.UpdateNoteTaskInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 16:40
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/integrateTaskInstances")
public class IntegrateTaskInstanceController {
    @Autowired
    IntegrateTaskInstanceService integrateTaskInstanceService;

    @RequestMapping (value = "/one/{id}", method = RequestMethod.GET)
    public JsonResult getOneAction(@PathVariable("actionId") String id,@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskInstanceService.getById(id));
    }

    @RequestMapping (value = "/{taskId}/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getByTaskId(@PathVariable("taskId") String taskId,@JwtTokenParser(key="userId") String userId,@PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(integrateTaskInstanceService.getAllByTaskId(taskId,userId,currentPage,pagesize));
    }

    @RequestMapping (value = "/user/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getByUserId(@JwtTokenParser(key="userId") String userId,@PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(integrateTaskInstanceService.getAllByOperatorId(userId,currentPage,pagesize));
    }


    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult create(@JwtTokenParser(key="userId") String userId, @RequestBody AddIntegrateTaskInstanceDTO add) {
        return ResultUtils.success(integrateTaskInstanceService.create(userId,add));
    }

    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("id") String id, @RequestBody UpdateIntegrateTaskInstanceDTO update, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskInstanceService.updateById(id, update, userId));
    }

    @RequestMapping(value = "/note/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateNote(@PathVariable("id") String id, @RequestBody UpdateNoteTaskInstanceDTO update, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskInstanceService.updateByNoteId(id, update, userId));
    }


    @RequestMapping (value = "/{actionId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("actionId") String actionId) {
        integrateTaskInstanceService.delete(actionId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getInstanceOfUncompleted/{taskId}", method = RequestMethod.GET)
    public JsonResult getInstanceOfUncompleted(@JwtTokenParser(key = "userId") String userId, @PathVariable String taskId) {
        return ResultUtils.success(integrateTaskInstanceService.getInstanceOfUncompleted(userId, taskId));
    }

    @RequestMapping(value = "/getInstanceXML/{Id}", method = RequestMethod.GET)
    public JsonResult getXML(@PathVariable String Id) {
        return ResultUtils.success(integrateTaskInstanceService.getXML(Id));
    }

    @RequestMapping(value = "/getTaskInfo/{id}", method = RequestMethod.GET)
    public JsonResult getTaskInfo(@PathVariable String id) {
        return ResultUtils.success(integrateTaskInstanceService.getTaskInfo(id));
    }

    @RequestMapping(value = "/getAllIntegrateTaskInstance/{taskId}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult getAllInstances(@PathVariable String taskId, @JwtTokenParser(key = "userId") String userId, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(integrateTaskInstanceService.getAllIntegrateTaskInstance(taskId, userId, page, size));
    }

    @RequestMapping(value = "/getSelectedInstancesByProjectId/{projectId}", method = RequestMethod.GET)
    public JsonResult getSelectedInstancesByProjectId(@PathVariable String projectId) {
        return ResultUtils.success(integrateTaskInstanceService.getSelectedInstancesByProjectId(projectId));
    }

    @RequestMapping(value = "/getAllInstancesOfReproductionByProjectId/{projectId}", method = RequestMethod.GET)
    public JsonResult getAllInstancesOfReproductionByProjectId(@PathVariable String projectId) {
        return ResultUtils.success(integrateTaskInstanceService.getAllInstancesOfReproductionByProjectId(projectId));
    }

}
