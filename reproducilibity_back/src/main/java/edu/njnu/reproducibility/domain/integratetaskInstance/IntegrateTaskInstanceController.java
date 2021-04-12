package edu.njnu.reproducibility.domain.integratetaskInstance;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.AddIntegrateTaskInstanceDTO;
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
        return ResultUtils.success(integrateTaskInstanceService.getAllByTaskId(taskId,currentPage,pagesize));
    }

    @RequestMapping (value = "/user/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getByUserId(@JwtTokenParser(key="userId") String userId,@PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(integrateTaskInstanceService.getAllByOperatorId(userId,currentPage,pagesize));
    }


    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult create(@JwtTokenParser(key="userId") String userId, @RequestBody AddIntegrateTaskInstanceDTO add) {
        return ResultUtils.success(integrateTaskInstanceService.create(userId,add));
    }


    @RequestMapping (value = "/{actionId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("actionId") String actionId) {
        integrateTaskInstanceService.delete(actionId);
        return ResultUtils.success();
    }
}
