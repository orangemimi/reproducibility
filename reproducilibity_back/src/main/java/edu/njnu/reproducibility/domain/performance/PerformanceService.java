package edu.njnu.reproducibility.domain.performance;

import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.performance.dto.AddPerformanceDTO;
import edu.njnu.reproducibility.domain.performance.dto.UpdatePerformanceContextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
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
        return performance;
    }

    public Performance getPerformanceByProjectId(String projectId) {
        Performance performance = performanceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        return performance;
    }

    public Object savePerformance(AddPerformanceDTO add, String userId) {
        Performance performance = new Performance();
        performance.setUserId(userId);
        add.convertTo(performance);
        return performanceRepository.insert(performance);
    }

    public Object updatePerformance(String type, String projectId, Content update) {

        Date date = new Date();
        update.setUpdateTime(date);
        Performance performance = performanceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        if (type.equals("context") ) {
            performance.getCompletion().getContext().add(update);
        }
        if(type.equals("resource") ){
            performance.getCompletion().getResource().add(update);
        }
        if(type.equals("scenario") ){
            performance.getCompletion().getScenario().add(update);
        }
        if(type.equals("results") ){
            performance.getCompletion().getResults().add(update);
        }

        return performanceRepository.save(performance);
    }

    public void deleteById(String id) {
        performanceRepository.deleteById(id);
    }
}
