package edu.njnu.reproducibility.domain.resourceItem.parameterSupport;

import edu.njnu.reproducibility.domain.resourceItem.ParameterResource;
import lombok.Data;

/**
 * @ClassName SliderParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class SliderParameter  extends ParameterResource {
    public String max;
    public String min;
    public String step;
    public String defaultValue;

}
