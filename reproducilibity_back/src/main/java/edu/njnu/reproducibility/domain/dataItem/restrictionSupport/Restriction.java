package edu.njnu.reproducibility.domain.dataItem.restrictionSupport;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/16 21:17
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Restriction {
    TypeEnums type;
    String decimal;
    ContentEnums content;
}