package edu.njnu.reproducibility.domain.dataService;

import edu.njnu.reproducibility.domain.model.support.ModelItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:14
 * @modified By：
 * @version: 1.0.0
 */
public interface DataServiceRepositroy  extends MongoRepository<DataService,String> {
    Page<DataService> findAll(Pageable pageable);
}
