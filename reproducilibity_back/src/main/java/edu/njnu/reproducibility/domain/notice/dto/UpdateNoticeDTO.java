package edu.njnu.reproducibility.domain.notice.dto;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.notice.Notice;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/10 21:36
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateNoticeDTO implements ToDomainConverter<Notice> {
    String state;   //read|unread
}
