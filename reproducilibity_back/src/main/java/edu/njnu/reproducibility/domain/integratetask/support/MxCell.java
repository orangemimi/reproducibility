package edu.njnu.reproducibility.domain.integratetask.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/14/14:56
 * @Description:
 */
@Data
public class MxCell {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    String id;
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    String name;
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    String type;
//    @JacksonXmlProperty(localName = "Object")
//    NodeAttribute nodeAttribute;
}
