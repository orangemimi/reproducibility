package edu.njnu.reproducibility.domain.integratetaskInstance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/6 16:41
 * @modified By：
 * @version: 1.0.0
 */
public interface IntegrateTaskInstanceRepository extends MongoRepository<IntegrateTaskInstance,String> {
    Optional<IntegrateTaskInstance> findById(String id);
    Page<IntegrateTaskInstance> findAllByOperatorId(String operatorId, PageRequest pageable);
    Page<IntegrateTaskInstance> findAllByTaskIdAndOperatorId(String taskId,String operator, PageRequest pageable);
    Optional<IntegrateTaskInstance> findByTaskId(String taskId);
    Optional<IntegrateTaskInstance> findByTid(String tid);

    @Query(sort = "{ createTime : -1 }")
    List<IntegrateTaskInstance> findAllByTaskIdAndOperatorId(String taskId, String operator);

    @Query(value = "{taskId: ?0, operatorId: {$ne: ?1}}", sort = "{ createTime : -1 }")
    List<IntegrateTaskInstance> findAllByTaskId(String taskId, String userId);

    int countByTaskIdAndOperatorId(String taskId, String operator);


}
