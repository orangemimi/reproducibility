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
public class AddResultCollectionDTO implements ToDomainConverter<ResultCollection> {
//    ResultEntity resultEntity;
    String name;
    String description;
    List<String> keywords;
    String duty;//output
    String format;//file
    String userId;//
    String projectId;
    String version;
    String value; //url or value    or existing file url /value
    ComparisonEntity comparisonEntity;
}
