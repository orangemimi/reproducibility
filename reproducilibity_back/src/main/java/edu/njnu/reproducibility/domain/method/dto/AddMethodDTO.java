package edu.njnu.reproducibility.domain.method.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.method.Method;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:47
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddMethodDTO implements ToDomainConverter<Method> {
    String projectId;
    String version;
}
