package edu.njnu.reproducibility.domain.data.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.data.support.DataItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateDataItemDTO implements ToDomainConverter<DataItem> {
    ArrayList<DataItem> children;
}
