package edu.njnu.reproducibility.domain.integratetask.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/01/14/14:52
 * @Description:
 */
@Data
public class Root {

    @JacksonXmlElementWrapper(localName = "mxCell")
    List<MxCell> mxCells;
}
