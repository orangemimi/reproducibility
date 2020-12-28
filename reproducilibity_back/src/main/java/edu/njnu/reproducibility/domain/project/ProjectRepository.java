package edu.njnu.reproducibility.domain.project;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:44
 * @modified By：
 * @version: 1.0.0
 */
public interface ProjectRepository extends MongoRepository<Project,String> {
    Optional<Project> findById(String id);
    Optional<Project> findByIdAndCreator(String id,String userId);
    List<Project> findByCreator(String creatorId);
    List<Project> findByPrivacyInOrCreator(List<String> privacyList,String creatorId);
}
