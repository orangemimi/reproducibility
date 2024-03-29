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


    @RequestMapping (value = "/recipient", method = RequestMethod.GET)
    public JsonResult getAllNoticeByRecipient(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getAllNoticeByRecipient(userId));
    }

    @RequestMapping (value = "/sender", method = RequestMethod.GET)
    public JsonResult getAllNoticeBySender(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getAllNoticeBySender(userId));
    }

    @RequestMapping (value = "/recipientTrue", method = RequestMethod.GET)
    public JsonResult getAllNoticeByRecipientTrue(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getAllNoticeByRecipientTrue(userId));
    }

    @RequestMapping (value = "/senderTrue", method = RequestMethod.GET)
    public JsonResult getAllNoticeBySenderTrue(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getAllNoticeBySenderTrue(userId));
    }

    @RequestMapping (value = "", method = RequestMethod.POST)
    public JsonResult save(@JwtTokenParser(key="userId") String userId, @RequestBody AddNoticeDTO add) {
        return ResultUtils.success(noticeService.save(userId,add));
    }

    @RequestMapping (value = "/{noticeId}/{state}", method = RequestMethod.PATCH)
    public JsonResult changeNoticeState(@PathVariable("noticeId") String noticeId,@PathVariable("state") String state,
                             @JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(noticeService.changeNoticeState(noticeId,state,userId));
    }

    @RequestMapping (value = "/{noticeId}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("noticeId") String noticeId) {
        noticeService.delete(noticeId);
        return ResultUtils.success();
    }

    @RequestMapping (value = "/delrec/{noticeId}", method = RequestMethod.PATCH)
    public JsonResult changeRecState(@PathVariable("noticeId") String noticeId) {
        return ResultUtils.success(noticeService.changeRecState(noticeId));
    }

    @RequestMapping (value = "/delsen/{noticeId}", method = RequestMethod.PATCH)
    public JsonResult changeSenState(@PathVariable("noticeId") String noticeId) {
        return ResultUtils.success(noticeService.changeSenState(noticeId));
    }

    @RequestMapping(value = "/getnumforapply/", method = RequestMethod.GET)
    public JsonResult getNumForApply(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getNumForApply(userId));
    }

    @RequestMapping(value = "/getnumforreply", method = RequestMethod.GET)
    public JsonResult getNumForReply(@JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(noticeService.getNumForReply(userId));
    }

    @RequestMapping(value = "/changeallreply", method = RequestMethod.PATCH)
    public JsonResult changeAllReplyisread(@JwtTokenParser(key="userId") String userId) {
        return ResultUtils.success(noticeService.changeAllReplyisread(userId));
    }
}
