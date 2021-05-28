package edu.njnu.reproducibility.domain.context;


import edu.njnu.reproducibility.common.exception.MyException;
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

    public ContextDefinition saveContextDefinition(AddContextDefinitionDTO addContextDefinitionDTO, String userId) {
        ContextDefinition contextDefinition = new ContextDefinition();
        contextDefinition.setUserId(userId);
        addContextDefinitionDTO.convertTo(contextDefinition);
        return  contextDefinitionRepository.insert(contextDefinition);
    }

    public ContextDefinition getContextDefinition(String projectId) {
        ContextDefinition contextDefinition = contextDefinitionRepository.findFirstByProjectId(projectId).orElseThrow(MyException::noObject);
        return  contextDefinition;
    }

    public ContextDefinition updateContextDefinition(String projectId, UpdateContextDefinitionDTO updateContextDefinitionDTO) {
        ContextDefinition contextDefinition = contextDefinitionRepository.findFirstByProjectId(projectId).orElseThrow(MyException::noObject);
        updateContextDefinitionDTO.updateTo(contextDefinition);
        return contextDefinitionRepository.save(contextDefinition);
    }
}
