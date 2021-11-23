package edu.njnu.reproducibility.domain.content;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/11:25
 * @Description:
 */
public interface ContentRepository extends MongoRepository<Content, String> {
    Content findByProjectId(String projectId);

}
