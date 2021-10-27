package edu.njnu.reproducibility.domain.integratetaskInstance.support;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/10/26/22:25
 * @Description:
 */
@Data
public class Processes {

    List<Process> completed;
    List<Process> running;
    List<Process> waiting;
    List<Process> failed;
}
