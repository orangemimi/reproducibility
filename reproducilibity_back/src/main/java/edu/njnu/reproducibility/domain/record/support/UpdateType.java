package edu.njnu.reproducibility.domain.record.support;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/07/19:55
 * @Description:
 */
@Data
public class UpdateType {
    String type;
    Date updateTime;
}
