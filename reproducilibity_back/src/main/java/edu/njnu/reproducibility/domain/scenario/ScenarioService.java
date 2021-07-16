package edu.njnu.reproducibility.domain.scenario;

import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.scenario.dto.AddScenarioDTO;
import edu.njnu.reproducibility.domain.scenario.dto.UpdateScenarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/1 22:42
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class ScenarioService {
    @Autowired
    ScenarioRepository scenarioRepository;

    public Optional<Scenario> getScenario(String projectId) {
        Optional<Scenario> scenario = scenarioRepository.findByProjectId(projectId);
        return  scenario;
    }

    public Scenario updateScenario(String projectId, UpdateScenarioDTO update) {
        Scenario scenario = scenarioRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        update.updateTo(scenario);
        return scenarioRepository.save(scenario);
    }

    public Scenario saveScenario(AddScenarioDTO add, String userId) {
        Scenario scenario = new Scenario();
        scenario.setUserId(userId);
        add.convertTo(scenario);
        return  scenarioRepository.insert(scenario);
    }
}
