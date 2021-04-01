package edu.njnu.reproducibility.domain.method;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:48
 * @modified By：
 * @version: 1.0.0
 */
public interface MethodRepository  extends MongoRepository<Method,String> {
    Optional<Method> findByProjectId(String projectId);
    Optional<Method> findById(String id);
}
