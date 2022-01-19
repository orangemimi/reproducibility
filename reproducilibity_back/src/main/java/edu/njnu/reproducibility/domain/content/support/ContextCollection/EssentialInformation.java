package edu.njnu.reproducibility.domain.content.support.ContextCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/10:59
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssentialInformation {
    String name;
    String purpose;
    String abstractText;
    String abstractRender;
    List<String> keyWords;
}
