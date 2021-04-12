package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/2 10:23
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class Event {
    String id;//cell id
    String reference;//event id;
    String name;
    String description;
    String type;//input-output-link--其中link只会在input上出现
    String dataRef;//data_id
}
