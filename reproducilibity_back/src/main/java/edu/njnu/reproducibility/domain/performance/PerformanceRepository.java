package edu.njnu.reproducibility.domain.performance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:43
 * @modified By：
 * @version: 1.0.0
 */
public interface PerformanceRepository extends MongoRepository<Performance,String> {
    Optional<Performance> findById(String id);
    Optional<Performance> findByProjectId(String id);

}
