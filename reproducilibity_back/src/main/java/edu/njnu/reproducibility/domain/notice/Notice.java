package edu.njnu.reproducibility.domain.notice;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/9 16:41
 * @modified By：
 * @version: 1.0.0
 */

@Data
@Document(collection = "Notice")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity {
    @Id
    String id;//notice id
    String recipientId;  //xxx@xx.xxx
    String senderId;
    String type;    //notice|reply|apply
    JSONObject content;
    String state;   //read|unread|approve|refuse
}
