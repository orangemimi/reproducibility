package edu.njnu.reproducibility.domain.context;

import edu.njnu.reproducibility.domain.context.support.Scale;
import edu.njnu.reproducibility.domain.context.support.StepConstraint;
import edu.njnu.reproducibility.domain.context.support.TemporalExtent;
import edu.njnu.reproducibility.domain.context.support.TemporalReference;
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
    Scale temporalScale;
    TemporalReference temporalReference;
    List<StepConstraint> stepConstraintList;
    List<TemporalExtent> temporalExtentList;
}
