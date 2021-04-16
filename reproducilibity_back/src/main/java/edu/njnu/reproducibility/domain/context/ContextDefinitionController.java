package edu.njnu.reproducibility.domain.context;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.context.dto.AddContextDefinitionDTO;
import edu.njnu.reproducibility.domain.context.dto.UpdateContextDefinitionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhiyi
 * @Date 2020/9/14  22:35
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/contextDefinition")
public class ContextDefinitionController {
    @Autowired
    ContextDefinitionService contextDefinitionService;

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public JsonResult getContextDefinition(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(contextDefinitionService.getContextDefinition(projectId));
    }

    @RequestMapping(value = "/{projectId}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateContextDefinition(@PathVariable("projectId") String projectId, @RequestBody UpdateContextDefinitionDTO updateContextDefinitionDTO) {
        return ResultUtils.success(contextDefinitionService.updateContextDefinition(projectId, updateContextDefinitionDTO));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveContextDefinition(@RequestBody AddContextDefinitionDTO addContextDefinitionDTO,@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(contextDefinitionService.saveContextDefinition(addContextDefinitionDTO,userId));
    }
}
