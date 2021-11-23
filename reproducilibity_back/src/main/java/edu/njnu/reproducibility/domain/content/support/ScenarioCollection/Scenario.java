package edu.njnu.reproducibility.domain.content.support.ScenarioCollection;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/22:32
 * @Description:
 */
@Data
public class Scenario {
    List<TextGroup> textGroups;
    List<PictureGroup> pictureGroups;
    List<VideoGroup> videoGroups;
    List<FormGroup> formGroups;
    List<MapGroup> mapGroups;
    List<FlowChartGroup> flowChartGroups;
}
