package edu.njnu.reproducibility.domain.integratetask.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/14/15:08
 * @Description:
 */
@Data
public class DataSelect {
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    String name;
    @JacksonXmlProperty(isAttribute = true, localName = "value")
    String value;
}
