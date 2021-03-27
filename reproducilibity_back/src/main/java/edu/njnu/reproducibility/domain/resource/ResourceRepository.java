package edu.njnu.reproducibility.domain.resource;


import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface ResourceRepository extends MongoRepository<Resource,String> {
    Optional<Resource> findFirstByProjectId(String pid);
}
