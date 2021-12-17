package edu.njnu.reproducibility.domain.content.support.ContextCollection;

import edu.njnu.reproducibility.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/11:02
 * @Description:
 */
@Data
public class TemporalInfo {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date end;

}
