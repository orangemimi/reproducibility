package edu.njnu.reproducibility.domain.projectResource;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface ProjectResourceRepository extends MongoRepository<ProjectResource,String> {
    Optional<ProjectResource> findFirstByProjectId(String pid);
    Optional<ProjectResource> findByProjectId(String pid);
}
