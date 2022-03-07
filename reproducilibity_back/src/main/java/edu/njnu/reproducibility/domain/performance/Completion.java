package edu.njnu.reproducibility.domain.performance;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/19 22:07
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Completion {
    List<Content> context;
    List<Content> resource;
    List<Content> scenario;
    List<Content> results;
}
