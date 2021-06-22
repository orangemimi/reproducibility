package edu.njnu.reproducibility.domain.dataItemCollection.support;

import edu.njnu.reproducibility.domain.dataItemCollection.ParameterDataCollection;
import lombok.Data;

/**
 * @ClassName RangeParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class RangeParameter extends ParameterDataCollection {
    public String from;
    public String to;
    public String defaultValue;

}
