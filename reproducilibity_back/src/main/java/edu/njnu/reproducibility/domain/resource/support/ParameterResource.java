package edu.njnu.reproducibility.domain.resource.support;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.restriction.ParameterRestriction;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/7 10:51
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ParameterResource extends DataItemCollection {
    ParameterRestriction restriction;
}
