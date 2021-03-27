package edu.njnu.reproducibility.domain.notice.dto;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.notice.Notice;
import edu.njnu.reproducibility.domain.project.Project;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/10 21:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddNoticeDTO implements ToDomainConverter<Notice> {
    String recipientId;  //xxx@xx.xxx
    String type;    //notice|reply|apply
    JSONObject content;
    String state;   //read|unread
}
