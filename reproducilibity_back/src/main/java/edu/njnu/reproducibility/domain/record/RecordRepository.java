package edu.njnu.reproducibility.domain.record;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:47
 * @Version 1.0.0
 */
public interface RecordRepository extends MongoRepository<Record,String> {
    Optional<Record> findById(String id);
    List<Record> findAllByProjectId(String projectId);
}
