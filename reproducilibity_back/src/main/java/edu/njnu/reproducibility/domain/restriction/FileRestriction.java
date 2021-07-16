package edu.njnu.reproducibility.domain.restriction;

import edu.njnu.reproducibility.domain.context.SpatialInfo;
import edu.njnu.reproducibility.domain.context.TemporalInfo;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/7 10:31
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class FileRestriction extends Restriction {
    SpatialInfo spatialInfo;
    TemporalInfo temporalInfo;
    String format;
}
