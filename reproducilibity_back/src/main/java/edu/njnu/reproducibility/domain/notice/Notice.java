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
    String state;   //read|unread|approve|refuse|ignore
    String recstate;        //true/false    recipient是否对此可见
    String senstate;        //true/false    sender是否对此可见
    String replyisread;     //true/false/unknown    回复是否已读,回复在等待对方回复的时候处在unknown状态
}
