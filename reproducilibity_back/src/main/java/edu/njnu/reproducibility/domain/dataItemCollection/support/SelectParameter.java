package edu.njnu.reproducibility.domain.dataItemCollection.support;

import edu.njnu.reproducibility.domain.dataItemCollection.ParameterDataCollection;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName SelectParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class SelectParameter  extends ParameterDataCollection {
    public Map<String,String> keyValue;
    public String defaultValue;
}
