package edu.njnu.reproducibility.domain.performance;

import lombok.Data;

import java.util.Date;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/19 22:49
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Content{
//    String type;//different
    String content;
    String degree;

    //style
    String type;
    String icon;
//    String color;
    Date updateTime;
}
