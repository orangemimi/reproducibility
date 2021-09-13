package edu.njnu.reproducibility.domain.notice;


import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.notice.dto.AddNoticeDTO;
import edu.njnu.reproducibility.domain.notice.dto.UpdateNoticeDTO;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.project.ProjectService;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.domain.user.User;
import edu.njnu.reproducibility.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Author ：Zhiyi
 * @Date ：2021/3/9 16:43
 * @modified By：
 * @version: 1.0.0
 */

@Service
public class NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    UserService userService;

    public Object changeNoticeState(String noticeId, String  state, String recipientId) {
        Notice notice = noticeRepository.findByIdAndRecipientId(noticeId, recipientId).orElseThrow(MyException::noObject);
        if(notice.getState().equals("unread") && !state.equals("unread")) {
            //项目拥有者回复请求时，要将删除的消息在发送者那重新展示
            if(notice.getSenstate().equals("false")) {
                notice.setSenstate("true");
            }
            notice.setReplyisread("false");
        }
        notice.setState(state);
        return noticeRepository.save(notice);
    }

    public Notice save(String userId, AddNoticeDTO add) {
        Notice notice = new Notice();
        add.convertTo(notice);
//        Creator creator = new Creator();
//        creator.setName(userName);
//        creator.setId(userId);
//        notice.setSenderId(userId);
        notice.setState("unread");
        notice.setSenderId(userId);
        notice.setRecstate("true");
        notice.setSenstate("true");
        notice.setReplyisread("unknown");
        return noticeRepository.insert(notice);
    }

    public void delete(String noiceId) {
        noticeRepository.deleteById(noiceId);
    }

    //根据Recipient获取所以Recipient的消息
    public List<Notice> getAllNoticeByRecipient(String receipantId) {
        List<Notice> temp = noticeRepository.findAllByRecipientId(receipantId);
        return temp;
    }

    //根据Recipient获取所以Recipient可见的消息
    public List<Notice> getAllNoticeByRecipientTrue(String receipantId) {
        List<Notice> temp = noticeRepository.findAllByRecipientId(receipantId);
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).recstate.equals("false")) {
                temp.remove(i);
                i--;
            } else {
                JSONObject content = temp.get(i).getContent();
                content.append("sendername", userService.getUserInfoById(temp.get(i).getSenderId()).getName());
                temp.get(i).setContent(content);
            }
        }
        return temp;
    }

    //根据Sender获取所以Sender可见的消息
    public List<Notice> getAllNoticeBySenderTrue(String senderId) {
        List<Notice> temp = noticeRepository.findAllBySenderId(senderId);
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).senstate.equals("false")) {
                temp.remove(i);
                i--;
            } else {
                JSONObject content = temp.get(i).getContent();
                content.append("recipientname", userService.getUserInfoById(temp.get(i).getRecipientId()).getName());
                temp.get(i).setContent(content);
            }
        }
        return temp;
    }

    //根据Sender获取所以Sender的消息
    public List<Notice> getAllNoticeBySender(String senderId) {
        return noticeRepository.findAllBySenderId(senderId);
    }

    public Notice get(String noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(MyException::noObject);
    }

    //使该消息对于Recipient不可见，即Recipient“删除”了该消息
    public Notice changeRecState(String noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(MyException::noObject);
        notice.setRecstate("false");
        return noticeRepository.save(notice);
    }

    //使该消息对于Sender不可见，即Sender“删除”了该消息
    public Notice changeSenState(String noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(MyException::noObject);
        notice.setSenstate("false");
        return noticeRepository.save(notice);
    }

    //获取用户未读的apply消息的数量
    public int getNumForApply(String userId) {
        List<Notice> notices = noticeRepository.findAllByRecipientId(userId);
        int num = 0;
        for(Notice notice : notices) {
            if(notice.getState().equals("unread")) { num++; }
        }
        return  num;
    }

    //获取用户的未读reply消息数量
    public int getNumForReply(String userId) {
        List<Notice> notices = noticeRepository.findAllBySenderId(userId);
        int num = 0;
        for(Notice notice : notices) {
            if(notice.getReplyisread().equals("false")) { num++; }
        }
        return num;
    }

    //改变回复消息是否已读的状态，只要用户点击到回复消息的页面，即认为回复消息已读了
    public int changeAllReplyisread(String userId) {
        List<Notice> notices = noticeRepository.findAllBySenderId(userId);
        for(Notice notice : notices) {
            if(notice.getReplyisread().equals("false")) {
                notice.setReplyisread("true");
                noticeRepository.save(notice);
            }
        }
        return 0;
    }
}
