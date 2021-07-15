package edu.njnu.reproducibility.domain.dataServiceCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:14
 * @modified By：
 * @version: 1.0.0
 */
public interface DataServiceCodeRepositroy extends MongoRepository<DataServiceCode,String> {
    Page<DataServiceCode> findAll(Pageable pageable);
}
