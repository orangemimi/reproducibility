package edu.njnu.reproducibility.domain.dataItemCollection;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface DataItemRepository extends MongoRepository<DataItemCollection,String> {
    Optional<DataItemCollection> findFirstByProjectId(String pid);
    List<DataItemCollection> findAllByProjectId(String pid);
}
