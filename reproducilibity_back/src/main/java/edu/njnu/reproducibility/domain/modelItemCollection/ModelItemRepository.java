package edu.njnu.reproducibility.domain.modelItemCollection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 14:33
 * @modified By：
 * @version:     1.0.0
 */
public interface ModelItemRepository extends MongoRepository<ModelItemColletion,String> {
    Page<ModelItemColletion> findAllByUserIdOrPrivacy(String userId, String privacy, Pageable pageable);
    Page<ModelItemColletion> findAllByPrivacy(String privacy, Pageable pageable);
    Page<ModelItemColletion> findAllByUserId(String userId, Pageable pageable);
//    void deleteByTid(String tid);
//    Optional<List<ModelItem>> findAllBy
    List<ModelItemColletion> findAllByProjectId(String projectId);
}
