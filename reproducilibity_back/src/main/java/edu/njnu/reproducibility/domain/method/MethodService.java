package edu.njnu.reproducibility.domain.method;

import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.method.dto.AddMethodDTO;
import edu.njnu.reproducibility.domain.method.dto.UpdateMethodDTO;
import edu.njnu.reproducibility.domain.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:48
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class MethodService {
    @Autowired
    MethodRepository methodRepository;

    public Object getByProjectId(String projectId) {
        return methodRepository.findByProjectId(projectId).orElse(null);
    }

    public Object update(String projectId, String userId, UpdateMethodDTO update) {
        Method method = methodRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        update.updateTo(method);
        return methodRepository.save(method);
    }

    public Object create(String userId, AddMethodDTO add) {
        Method method  = new Method();
        add.convertTo(method);
//        Creator creator = new Creator();
//        creator.setName(userName);
//        creator.setId(userId);
        method.setCreator(userId);
        return methodRepository.insert(method);
    }
}
