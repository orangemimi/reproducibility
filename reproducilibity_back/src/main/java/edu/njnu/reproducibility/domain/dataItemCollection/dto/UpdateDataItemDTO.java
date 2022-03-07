package edu.njnu.reproducibility.domain.dataItemCollection.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.dataItemCollection.support.ActivityAttribute;
import edu.njnu.reproducibility.domain.dataItemCollection.support.AgentAttribute;
import edu.njnu.reproducibility.domain.restriction.Restriction;
import lombok.Data;

import java.util.List;

@Data
public class UpdateDataItemDTO implements ToDomainConverter<DataItemCollection> {
    String name;
    String format;
    String description;
    String type;
    String value;
}
