package edu.njnu.reproducibility.domain.integratetask;



import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.integratetask.dto.AddIntegrateTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateCurrentActionInTaskDTO;
import edu.njnu.reproducibility.domain.integratetask.dto.UpdateIntegratedTaskDTO;
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
    public JsonResult getTask(@PathVariable("id") String id, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskService.getModelTaskInfo(id));
    }

    @RequestMapping(value = "/all/{pid}", method = RequestMethod.GET)
    public JsonResult getAllTask(@PathVariable("pid") String pid) {
        return ResultUtils.success(integrateTaskService.getAllModelTaskInfo(pid));
    }
    //model action数据配置引发更新，其他的均可选另存为一个task，或更新
    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateTask(@PathVariable("id") String id, @RequestBody UpdateIntegratedTaskDTO UpdateResourceDTO, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskService.updateModelTaskInfo(id, UpdateResourceDTO,userId));
    }

<<<<<<< HEAD

    @RequestMapping(value = "/changeSelectInstance/{id}/{instanceId}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult changeSelectInstance(@PathVariable("id") String id, @PathVariable("instanceId") String instanceId, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(integrateTaskService.changeSelectInstance(id, instanceId, userId));
=======
    @RequestMapping(value = "/selectAction/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateSelectAction(@PathVariable("id") String id, @RequestBody UpdateCurrentActionInTaskDTO UpdateResourceDTO, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskService.updateSelectAction(id, UpdateResourceDTO,userId));
    }

    @RequestMapping(value = "/cancleAction/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult cancleSelectAction(@PathVariable("id") String id, @RequestBody UpdateCurrentActionInTaskDTO UpdateResourceDTO, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskService.cancleSelectAction(id, UpdateResourceDTO,userId));
>>>>>>> parent of f11cd19 (mxgraph)
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveTask(@RequestBody AddIntegrateTaskDTO add, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(integrateTaskService.saveModelTaskInfo(add,userId));
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteTask(@PathVariable("id") String id){
        integrateTaskService.deleteById(id);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/run", method = RequestMethod.POST)
    public JsonResult runTask(@RequestParam("file") MultipartFile file,
                              @RequestParam("taskName") String taskName,
                              HttpServletRequest request) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if(session.getAttribute("userId")==null) {
            return ResultUtils.error(-1, "no login");
        }
        else {
            String username = session.getAttribute("userName").toString();
            return ResultUtils.success(integrateTaskService.runTask(file, taskName,username));
        }
    }
}
