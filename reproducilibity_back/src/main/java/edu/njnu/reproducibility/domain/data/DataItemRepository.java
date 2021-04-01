package edu.njnu.reproducibility.domain.data;

import edu.njnu.reproducibility.domain.data.support.DataItem;
import edu.njnu.reproducibility.domain.resource.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface DataItemRepository extends MongoRepository<DataItem,String> {
    Optional<DataItem> findById(String pid);
    List<DataItem> findAllByUploaderId(String id);
//    Optional<List<DataItem>> findAllByStepBindId(String stepId);
}
