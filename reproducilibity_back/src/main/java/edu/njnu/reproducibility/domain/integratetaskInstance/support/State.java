package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/2 10:23
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class State {
    String id;
    String name;
    String description;
    List<Event> eventList;
}
