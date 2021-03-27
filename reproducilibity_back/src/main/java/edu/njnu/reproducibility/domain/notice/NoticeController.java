package edu.njnu.reproducibility.domain.notice;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.notice.dto.AddNoticeDTO;
import edu.njnu.reproducibility.domain.notice.dto.UpdateNoticeDTO;
import edu.njnu.reproducibility.domain.project.ProjectService;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/9 16:41
 * @modified By：
 * @version: 1.0.0
 */

@RestController
@RequestMapping(value = "/notices")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @RequestMapping (value = "/one/{noticeId}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("noticeId") String noticeId) {
        return ResultUtils.success(noticeService.get(noticeId));
    }


    @RequestMapping (value = "/", method = RequestMethod.GET)
    public JsonResult getAllNotice(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getAllNotice(userId));
    }


    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult save(@JwtTokenParser(key="userId") String userId, @RequestBody AddNoticeDTO add) {
        return ResultUtils.success(noticeService.save(userId,add));
    }

    @RequestMapping (value = "/{noticeId}", method = RequestMethod.PATCH)
    public JsonResult readNotice(@PathVariable("noticeId") String noticeId,
                             @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(noticeService.readNotice(noticeId,userId));
    }

    @RequestMapping (value = "/{noticeId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("noticeId") String noticeId) {
        noticeService.delete(noticeId);
        return ResultUtils.success();
    }


}
