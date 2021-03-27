package edu.njnu.reproducibility.domain.resource.spatialTemporalInfo;

import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class TemporalInfo {
    String enable;
    Scale scale;
    TimeReference timeReference;
    List<StepConstraint> stepConstraint;
    List<TimeExent> timeExentList;
}
