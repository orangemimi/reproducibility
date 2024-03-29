package edu.njnu.reproducibility.domain.integratetaskInstance.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import edu.njnu.reproducibility.domain.integratetask.support.Action;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstance;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:07
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateIntegrateTaskInstanceDTO implements ToDomainConverter<IntegrateTaskInstance> {

    Action action;

    //模型运行所需的参数 以及
    Integer status; // 0代表未开始，-1代表运行失败，1代表运行成功, 2代表运行超时(不存在运行中状态，省略)
    String authority;
}
