package edu.njnu.reproducibility.domain.dataService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:14
 * @modified By：
 * @version: 1.0.0
 */
public interface DataServiceRepositroy  extends MongoRepository<DataService,String> {
    Page<DataService> findAll(Pageable pageable);
    List<DataService> findAllByUserId(String userId);

    List<DataService> findAllByProjectId(String projectId);
}
