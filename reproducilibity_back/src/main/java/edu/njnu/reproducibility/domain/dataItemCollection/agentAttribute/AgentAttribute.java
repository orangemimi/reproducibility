package edu.njnu.reproducibility.domain.dataItemCollection.agentAttribute;

import lombok.Data;

import java.util.Map;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/17 15:10
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AgentAttribute {
    Map<String,String> organization;
    String reference;
}
