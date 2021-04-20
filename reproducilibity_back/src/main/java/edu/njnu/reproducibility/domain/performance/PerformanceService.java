package edu.njnu.reproducibility.domain.performance;

import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.performance.dto.AddPerformanceDTO;
import edu.njnu.reproducibility.domain.performance.dto.UpdatePerformanceDTO;
import edu.njnu.reproducibility.domain.record.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/17 13:43
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class PerformanceService {
    @Autowired
    PerformanceRepository performanceRepository;

    public Performance getPerformanceById(String id) {
        Performance performance = performanceRepository.findById(id).orElseThrow(MyException::noObject);
        return  performance;
    }

    public Performance getPerformanceByProjectId(String projectId) {
        Performance performance = performanceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject) ;
        return  performance;
    }

    public Object savePerformance(AddPerformanceDTO add, String userId) {
        Performance performance = new Performance();
        performance.setUserId(userId);
        add.convertTo(performance);
        return performanceRepository.insert(performance);
    }

    public Object updatePerformance(String id, UpdatePerformanceDTO update, String userId) {
        Performance performance = performanceRepository.findById(id).orElseThrow(MyException::noObject) ;
        update.updateTo(performance);
        update.getCompletion().setUpdateTime(new Date());


        return  performanceRepository.save(performance);

    }

    public void deleteById(String id) {
        performanceRepository.deleteById(id);
    }
}
