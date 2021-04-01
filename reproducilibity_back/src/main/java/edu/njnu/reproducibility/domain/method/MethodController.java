package edu.njnu.reproducibility.domain.method;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.method.dto.AddMethodDTO;
import edu.njnu.reproducibility.domain.method.dto.UpdateMethodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/31 15:48
 * @modified By：
 * @version: 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/methods")
public class MethodController {
    @Autowired
    MethodService methodService;

    @RequestMapping (value = "/one/{projectId}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("projectId") String projectId) {
        return ResultUtils.success(methodService.getByProjectId(projectId));
    }


    @RequestMapping (value = "/{projectId}", method = RequestMethod.PATCH)
    public JsonResult update(@PathVariable("projectId") String projectId,
                             @JwtTokenParser(key="userId") String userId,
                             @RequestBody UpdateMethodDTO update) {
        return ResultUtils.success(methodService.update(projectId,userId,update));
    }

    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult create(@JwtTokenParser(key="userId") String userId, @RequestBody AddMethodDTO add) {
        return ResultUtils.success(methodService.create(userId,add));
    }

}
