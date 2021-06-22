package edu.njnu.reproducibility.domain.dataItemCollection.support;

import edu.njnu.reproducibility.domain.dataItemCollection.ParameterDataCollection;
import lombok.Data;

import java.util.List;

/**
 * @ClassName TableParameter
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/3/18
 * @Version 1.0.0
 */
@Data
public class TableParameter extends ParameterDataCollection {
    public String defaultValue;
    public List<String> fields;
}
