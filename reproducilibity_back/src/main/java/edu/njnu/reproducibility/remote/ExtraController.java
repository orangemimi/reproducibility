package edu.njnu.reproducibility.remote;

import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/12/14/20:10
 * @Description:
 */

@RestController
@RequestMapping("/extra")
public class ExtraController {

    @Autowired
    ExtraService extraService;

    //百度API接口获取行政区，前端以舍弃使用，直接读取本地的json文件
    @RequestMapping(value = "/getCityList", method = RequestMethod.GET)
    public JsonResult getCityList() {
        return ResultUtils.success(extraService.getCityList());
    }
}
