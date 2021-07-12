package edu.njnu.reproducibility.domain.resource.support;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.restriction.FileRestriction;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/7 10:27
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class FileResource extends DataItemCollection {
    String size;
    FileRestriction restriction;
}
