package edu.njnu.reproducibility.remote.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ZhangYiming
 * @Date: 2021/08/17/16:07
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteDataServiceInfo {
    @Id
    String id;
    String uid;
    String userToken;
    String type;
    String parentLevel;
    String workSpace;
}
