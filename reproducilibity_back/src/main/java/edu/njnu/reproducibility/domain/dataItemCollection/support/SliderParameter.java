package edu.njnu.reproducibility.domain.dataItemCollection.support;

import edu.njnu.reproducibility.domain.dataItemCollection.ParameterDataCollection;
import lombok.Data;

/**
 * @ClassName SliderParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class SliderParameter  extends ParameterDataCollection {
    public String max;
    public String min;
    public String step;
    public String defaultValue;

}
