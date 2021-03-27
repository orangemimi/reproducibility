package edu.njnu.reproducibility.domain.context;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.context.dto.AddContextDefinitionDTO;
import edu.njnu.reproducibility.domain.context.dto.UpdateContextDefinitionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/9/14  22:36
 * @Version 1.0.0
 */
@Service
public class ContextDefinitionService {
    @Autowired
    ContextDefinitionRepository contextDefinitionRepository;

    public JsonResult saveContextDefinition(AddContextDefinitionDTO addContextDefinitionDTO,String userId) {
        ContextDefinition contextDefinition = new ContextDefinition();
        contextDefinition.setUserId(userId);
        addContextDefinitionDTO.convertTo(contextDefinition);
        return  ResultUtils.success(contextDefinitionRepository.insert(contextDefinition));
    }

    public JsonResult getContextDefinition(String pid) {
        Optional<ContextDefinition> contextDefinition = contextDefinitionRepository.findFirstByProjectId(pid);
        return  ResultUtils.success(contextDefinition);
    }

    public JsonResult updateContextDefinition(String pid, UpdateContextDefinitionDTO updateContextDefinitionDTO) {
        ContextDefinition contextDefinition = contextDefinitionRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateContextDefinitionDTO.updateTo(contextDefinition);
        return ResultUtils.success(contextDefinitionRepository.save(contextDefinition));
    }
}
