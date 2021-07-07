package edu.njnu.reproducibility.domain.projectResource.tool.dto;


import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.projectResource.tool.ToolItem;
import lombok.Data;

import java.util.ArrayList;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 11:26
 * @modified By：
 * @version:     1.0.0
 */

@Data
public class AddToolItemDTO implements ToDomainConverter<ToolItem> {
    String userId;
    String projectId;

    String tid;
    String toolName;
    String toolUrl;//stateId, oid,mdlId,模型条目？
    JSONObject modelInfo; //stateId, oid,mdlId,模型条目？
    String description; // 可在多个不同的toolset内，或没有dataset
    ArrayList<String> recomStep; // step类型 or general
    ArrayList<String> categoryTag;
    String provider;
    String privacy;
    String toolImg;
    String detail;
}
