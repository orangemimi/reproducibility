package edu.njnu.reproducibility.domain.dataService;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/dataServices")
public class DataServiceController {
    @Autowired
    DataServiceService dataServiceService;

    @RequestMapping(value = "/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(dataServiceService.getAll(userId, currentPage, pagesize));
    }
}
