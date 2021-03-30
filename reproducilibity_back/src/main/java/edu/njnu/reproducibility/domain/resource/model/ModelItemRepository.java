package edu.njnu.reproducibility.domain.resource.model;

import edu.njnu.reproducibility.domain.resource.tool.ToolItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 14:33
 * @modified By：
 * @version:     1.0.0
 */
public interface ModelItemRepository extends MongoRepository<ModelItem,String> {
    Page<ModelItem> findAllByUploaderIdOrPrivacy(String userId, String privacy,Pageable pageable);
    Page<ModelItem> findAllByPrivacy( String privacy,Pageable pageable);
//    void deleteByTid(String tid);
//    Optional<List<ModelItem>> findAllBy
}
