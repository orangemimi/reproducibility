package edu.njnu.reproducibility.domain.resourceItem.parameterSupport;

import edu.njnu.reproducibility.domain.resourceItem.ParameterResource;
import lombok.Data;

/**
 * @ClassName RangeParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class RangeParameter extends ParameterResource {
    public String from;
    public String to;
    public String defaultValue;

}
