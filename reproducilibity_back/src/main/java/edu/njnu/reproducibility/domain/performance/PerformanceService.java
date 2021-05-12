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

    public Object updatePerformance(String type, String projectId, Content update, String userId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Performance performance = performanceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        if (type.equals("context") ) {
            performance.getCompletion().setContext(update);
            performance.getCompletion().getContext().setUpdateTime(date);
        }
        if(type.equals("resource") ){
            performance.getCompletion().setResource(update);
            performance.getCompletion().getResource().setUpdateTime(date);
        }
        if(type.equals("scenario") ){
            performance.getCompletion().setScenario(update);
            performance.getCompletion().getScenario().setUpdateTime(date);
        }
        if(type.equals("results") ){
            performance.getCompletion().setResults(update);
            performance.getCompletion().getResults().setUpdateTime(date);
        }
        return performanceRepository.save(performance);
    }

    public void deleteById(String id) {
        performanceRepository.deleteById(id);
    }
}
