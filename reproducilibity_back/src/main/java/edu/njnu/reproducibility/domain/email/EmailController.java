package edu.njnu.reproducibility.domain.email;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/9 16:41
 * @modified By：
 * @version: 1.0.0
 */

@RestController
@RequestMapping(value = "/emails")
public class EmailController {
    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/invite", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public JsonResult inviteEmail(@RequestBody Email email, @JwtTokenParser(key = "userId") String userId, @JwtTokenParser(key = "name") String name) {
        String emailContent = email.getMailContent()+ email.getRecipient();
        email.setMailContent(emailContent);
        email.setSender(userId);
        return ResultUtils.success(emailService.sendEmail(email));
    }

    @RequestMapping(value = "/send", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public JsonResult sendEmail(@RequestBody Email email, @JwtTokenParser(key = "userId") String userId) {
        return ResultUtils.success(emailService.sendEmail(email));
    }
}
