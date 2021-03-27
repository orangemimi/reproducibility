package edu.njnu.reproducibility.common.controller;



import edu.njnu.reproducibility.common.untils.JsonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @InterfaceName BaseController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface BaseController<AD,FD,UD> {
    @RequestMapping (value = "", method = RequestMethod.POST)
    JsonResult add(@RequestBody @Validated AD a);

    @RequestMapping (value = "/{id}", method = RequestMethod.DELETE)
    JsonResult delete(@PathVariable ("id") String id);

    @RequestMapping (value = "", method = RequestMethod.GET)
    JsonResult list(FD findDTO);

    @RequestMapping (value = "/count", method = RequestMethod.GET)
    JsonResult count();

    @RequestMapping (value = "/{id}", method = RequestMethod.GET)
    JsonResult get(@PathVariable ("id") String id);

    @RequestMapping (value = "/{id}", method = RequestMethod.PUT)
    JsonResult update(@PathVariable ("id") String id, @RequestBody UD updateDTO) throws ParserConfigurationException, SAXException, IOException;
}
