package edu.njnu.reproducibility.domain.performance;

import lombok.Data;

import java.util.Date;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/19 22:07
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Completion {
    Content context;
    Content resource;
    Content scenario;
    Content results;
}
