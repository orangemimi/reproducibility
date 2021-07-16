package edu.njnu.reproducibility.domain.scenario;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.scenario.dto.AddScenarioDTO;
import edu.njnu.reproducibility.domain.scenario.dto.UpdateScenarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/13 14:15
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/scenario")
public class ScenarioController {
    @Autowired
    ScenarioService scenarioService;

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public JsonResult getScenarioInfoByProjectId(@PathVariable("projectId") String projectId) {
        return ResultUtils.success( scenarioService.getScenario(projectId) );
    }

    @RequestMapping(value = "/{projectId}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateScenarioInfo(@PathVariable("projectId") String projectId, @RequestBody UpdateScenarioDTO update) {
        return ResultUtils.success(scenarioService.updateScenario(projectId, update));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveScenarioInfo(@RequestBody AddScenarioDTO add, @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(scenarioService.saveScenario(add,userId));
    }
}
