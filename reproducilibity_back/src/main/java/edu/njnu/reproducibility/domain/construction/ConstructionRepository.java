package edu.njnu.reproducibility.domain.construction;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/1 22:43
 * @modified By：
 * @version: 1.0.0
 */
public interface ConstructionRepository extends MongoRepository<Construction,String> {
    Optional<Construction> findByProjectId(String projectId);
}
