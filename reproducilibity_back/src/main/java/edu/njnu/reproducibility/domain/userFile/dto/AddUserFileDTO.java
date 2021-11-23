package edu.njnu.reproducibility.domain.userFile.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.userFile.UserFile;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/22/13:24
 * @Description:
 */
@Data
public class AddUserFileDTO implements ToDomainConverter<UserFile> {
    String name;
    String type;
    String size;
    String fid;
    String storey;
    String url;
    String userId;
    String state;
    List<String> children;
}
