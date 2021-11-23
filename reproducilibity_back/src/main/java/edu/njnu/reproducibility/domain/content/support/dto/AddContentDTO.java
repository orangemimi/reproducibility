package edu.njnu.reproducibility.domain.content.support.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.content.Content;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.Context;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Resource;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/17:31
 * @Description:
 */
@Data
public class AddContentDTO implements ToDomainConverter<Content> {
    String projectId;
    Context context;
    Resource resource;
}
