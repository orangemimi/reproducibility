package edu.njnu.reproducibility.domain.integratetask.support;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:02
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Conclusion {
    String text = "";
    String paper = "";
    List<String> pictures = new ArrayList<>();
//    Video video = new Video();
}

