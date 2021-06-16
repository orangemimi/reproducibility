package edu.njnu.reproducibility.domain.resourceItem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.njnu.reproducibility.domain.resourceItem.parameterSupport.*;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2021/6/15 22:26
 * @modified By：
 * @version: 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "form",visible =true,defaultImpl = InputParameter.class)
@JsonSubTypes({@JsonSubTypes.Type(value = InputParameter.class, name = "input_parameter")
        , @JsonSubTypes.Type(value = RangeParameter.class, name = "range_parameter"),
        @JsonSubTypes.Type(value = SliderParameter.class, name = "slider_parameter"),
        @JsonSubTypes.Type(value = SelectParameter.class, name = "select_parameter"),
        @JsonSubTypes.Type(value = TableParameter.class, name = "table_parameter"),
})
public class ParameterResource extends ResourceItem{
    public String name;
    public String form;//=type 别名
    public String description;
    public String tooltip;
    public String value;
}
