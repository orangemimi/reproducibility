package edu.njnu.reproducibility.domain.dataService;

import edu.njnu.reproducibility.domain.dataService.dto.AddDataServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */

@Service
public class DataServiceService {
    @Autowired
    DataServiceRepositroy dataServiceRepositroy;

    public Page<DataService> getAll(String userId, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<DataService> dataServiceList = dataServiceRepositroy.findAll( pageable);
        return dataServiceList;
    }

    public DataService save(String userId ,AddDataServiceDTO add) {
        DataService dataService = new DataService();

        add.convertTo(dataService);
        dataService.setUserId(userId);
        return dataServiceRepositroy.insert(dataService);
    }
}
