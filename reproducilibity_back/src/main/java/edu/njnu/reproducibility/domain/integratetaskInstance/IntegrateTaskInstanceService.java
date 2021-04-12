package edu.njnu.reproducibility.domain.integratetaskInstance;

import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.integratetaskInstance.dto.AddIntegrateTaskInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public IntegrateTaskInstance getById(String id) {
        IntegrateTaskInstance integrateTaskInstance = integrateTaskInstanceRepository.findById(id).orElseThrow(MyException::noObject);
        return integrateTaskInstance;
    }


    public Page<IntegrateTaskInstance>  getAllByTaskId(String taskId, int currentPage, int pagesize ) {
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        Page<IntegrateTaskInstance> actionList= integrateTaskInstanceRepository.findAllByTaskId(taskId,pageable);
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
}
