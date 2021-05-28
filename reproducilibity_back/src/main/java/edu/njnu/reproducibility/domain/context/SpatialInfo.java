package edu.njnu.reproducibility.domain.context;


import edu.njnu.reproducibility.domain.context.support.ResolutionConstraint;
import edu.njnu.reproducibility.domain.context.support.Scale;
import edu.njnu.reproducibility.domain.context.support.SpatialExtent;
import edu.njnu.reproducibility.domain.context.support.SpatialReference;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:16
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class SpatialInfo {
    Boolean enable;
    String spatialDimension;//2/3 维度
    Scale spatialScale;//尺度
    SpatialReference spatialReference;//空间参考系
    List<ResolutionConstraint> resolutionConstraintList;
    List<SpatialExtent> spatialExtentList;
}
