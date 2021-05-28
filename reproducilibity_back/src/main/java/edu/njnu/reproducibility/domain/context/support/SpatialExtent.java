package edu.njnu.reproducibility.domain.context.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 15:34
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class SpatialExtent {
    String name;
    String type;//Text/Envelope/Polygon
    String value;
    SpatialReference spatialReference;
}
