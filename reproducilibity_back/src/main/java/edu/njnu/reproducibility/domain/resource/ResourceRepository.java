package edu.njnu.reproducibility.domain.resource;


import edu.njnu.reproducibility.domain.projectResource.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface ResourceRepository extends MongoRepository<edu.njnu.reproducibility.domain.projectResource.Resource,String> {
    Optional<edu.njnu.reproducibility.domain.projectResource.Resource> findFirstByProjectId(String pid);
    Optional<Resource> findByProjectId(String pid);
}
