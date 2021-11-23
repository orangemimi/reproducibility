package edu.njnu.reproducibility.domain.content.support.ContextCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/10:59
 * @Description:
 */
@Data
public class EssentialInformation {

    String name;
    String purpose;
    String Abstract;
    List<String> keyWords;
}
