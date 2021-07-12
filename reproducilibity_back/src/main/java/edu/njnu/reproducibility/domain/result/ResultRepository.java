package edu.njnu.reproducibility.domain.result;


import edu.njnu.reproducibility.domain.projectResource.ProjectResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface ResultRepository extends MongoRepository<ResultCollection,String> {
    Optional<ResultCollection> findFirstByProjectId(String pid);
    Optional<ResultCollection> findByProjectId(String pid);
}
