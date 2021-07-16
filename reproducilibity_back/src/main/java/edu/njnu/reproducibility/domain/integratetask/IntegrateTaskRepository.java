package edu.njnu.reproducibility.domain.integratetask;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:47
 * @Version 1.0.0
 */
public interface IntegrateTaskRepository extends MongoRepository<IntegrateTask,String> {
    Optional<IntegrateTask> findById(String id);
    List<IntegrateTask> findAllByProjectId(String userId);
}
