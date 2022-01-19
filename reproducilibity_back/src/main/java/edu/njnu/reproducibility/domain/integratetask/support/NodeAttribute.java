package edu.njnu.reproducibility.domain.integratetask.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/14/15:05
 * @Description:
 */
@Data
public class NodeAttribute {
    @JacksonXmlProperty(isAttribute = true, localName = "doi")
    String doi;
    @JacksonXmlProperty(isAttribute = true, localName = "md5")
    String md5;
    @JacksonXmlProperty(isAttribute = true, localName = "isParameter")
    String isParameter;
    @JacksonXmlProperty(localName = "dataSelect")
    DataSelect dataSelect;
}
