package edu.njnu.reproducibility.domain.integratetask.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/14/14:47
 * @Description:
 */
@Data
@JacksonXmlRootElement(localName = "mxGraphModel")
public class MxGraphModel {
    @JacksonXmlElementWrapper(localName = "root")
    @JacksonXmlProperty(localName = "mxCell")
    List<MxCell> mxCells;
}
