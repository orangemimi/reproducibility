package edu.njnu.reproducibility.domain.context;


import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
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
@RequestMapping(value = "/context")
public class ContextDefinitionController {
    @Autowired
    ContextDefinitionService contextDefinitionService;

    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    public JsonResult getStepInfo(@PathVariable("pid") String pid) {
        return contextDefinitionService.getContextDefinition(pid);
    }

    @RequestMapping(value = "/{pid}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PATCH)
    public JsonResult updateStepInfo(@PathVariable("pid") String pid, @RequestBody UpdateContextDefinitionDTO updateContextDefinitionDTO) {
        return contextDefinitionService.updateContextDefinition(pid, updateContextDefinitionDTO);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult saveStepInfo(@RequestBody AddContextDefinitionDTO addContextDefinitionDTO,@JwtTokenParser(key="userId") String userId) {
        return contextDefinitionService.saveContextDefinition(addContextDefinitionDTO,userId);
    }
}
