package edu.njnu.reproducibility.domain.result.dto;

import edu.njnu.reproducibility.common.dto.ToDomainConverter;
import edu.njnu.reproducibility.domain.result.ResultCollection;
import edu.njnu.reproducibility.domain.result.support.ComparisonEntity;
import edu.njnu.reproducibility.domain.result.support.ResultEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/7/8 20:50
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class UpdateResultCollectionDTO implements ToDomainConverter<ResultCollection> {
//    ResultEntity resultEntity;
    ComparisonEntity comparisonEntity;
}
