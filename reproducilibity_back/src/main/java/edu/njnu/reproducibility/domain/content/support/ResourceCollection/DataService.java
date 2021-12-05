package edu.njnu.reproducibility.domain.content.support.ResourceCollection;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/30/19:05
 * @Description:
 */

@Data
public class DataService {
    String uid;
    String name;
    String description;
    String markDown;
    String markDownHtml;
}
