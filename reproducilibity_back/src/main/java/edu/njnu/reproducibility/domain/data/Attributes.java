package edu.njnu.reproducibility.domain.data;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/23 16:03
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Attributes {
    String name;
    String alia;//别名
    String type;//suffix
    String description;
    String url;
    String source;
    Boolean isDirect; //false--中间处理数据 //true--直接上传数据
}
