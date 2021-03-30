package edu.njnu.reproducibility.domain.integratetask;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:47
 * @Version 1.0.0
 */
public interface IntegrateModelTaskRepository extends MongoRepository<IntegrateModelTask,String> {
    Optional<IntegrateModelTask> findFirstById(String id);
    List<IntegrateModelTask> findAllByProjectId(String userId);
}
