package edu.njnu.reproducibility.domain.file.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.file.FileItem;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UpdateFileItemDTO implements ToDomainConverter<FileItem> {
    ArrayList<FileItem> children;
}
