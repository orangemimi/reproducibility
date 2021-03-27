package edu.njnu.reproducibility.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/9 16:41
 * @modified By：
 * @version: 1.0.0
 */

@Data
@Document(collection = "Email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {
    String recipient;  //xxx@xx.xxx
    String sender;// senderId
    String mailTitle;
    String mailContent;
}
