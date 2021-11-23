package edu.njnu.reproducibility.domain.userFile;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.userFile.dto.AddUserFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/22/13:28
 * @Description:
 */
@RestController
@RequestMapping("/UserFile")
public class UserFileController {
    @Autowired
    UserFileService userFileService;

    @RequestMapping(value = "/addUserFile", method = RequestMethod.POST)
    public JsonResult addUserFile(@RequestBody AddUserFileDTO addUserFileDTO) {
        return ResultUtils.success(userFileService.addUserFile(addUserFileDTO));
    }

    @RequestMapping(value = "/getUserFileByFidAndStorey/{fid}/{storey}", method = RequestMethod.GET)
    public JsonResult getUserFileByFidAndStorey(@PathVariable String fid, @PathVariable String storey, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(userFileService.getUserFileByFidAndStorey(fid, storey, userId));
    }
}
