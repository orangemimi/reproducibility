package edu.njnu.reproducibility.domain.method.support;

import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:56
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Link {
    String id;
    String name;
    From from;
    To to;
    List<InheritedData> inheritedDataList;

}
