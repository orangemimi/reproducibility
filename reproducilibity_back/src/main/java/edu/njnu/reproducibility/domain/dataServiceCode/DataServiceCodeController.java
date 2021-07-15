package edu.njnu.reproducibility.domain.dataServiceCode;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataService.dto.AddDataServiceDTO;
import edu.njnu.reproducibility.domain.dataServiceCode.dto.AddDataServiceCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/dataServiceCode")
public class DataServiceCodeController {
    @Autowired
    DataServiceCodeService dataServiceCodeService;

    @RequestMapping(value = "/{currentPage}/{pagesize}", method = RequestMethod.GET)
    public JsonResult getAll(@JwtTokenParser(key = "userId") String userId, @PathVariable("currentPage") int currentPage, @PathVariable("pagesize") int pagesize) {
        return ResultUtils.success(dataServiceCodeService.getAll(userId, currentPage, pagesize));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public JsonResult saveService(@JwtTokenParser(key = "userId") String userId,@RequestBody AddDataServiceCodeDTO add) throws IOException {
        return ResultUtils.success(dataServiceCodeService.save(userId,add));
    }
}
