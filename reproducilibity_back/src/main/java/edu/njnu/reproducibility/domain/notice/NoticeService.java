package edu.njnu.reproducibility.domain.notice;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.notice.dto.AddNoticeDTO;
import edu.njnu.reproducibility.domain.notice.dto.UpdateNoticeDTO;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Object changeNoticeState(String noticeId, String  state, String recipientId) {
        Notice notice = noticeRepository.findByIdAndRecipientId(noticeId, recipientId).orElseThrow(MyException::noObject);
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
        return noticeRepository.insert(notice);
    }

    public void delete(String noiceId) {
        noticeRepository.deleteById(noiceId);
    }

    public List<Notice> getAllNoticeByRecipient(String receipantId) {
        return noticeRepository.findAllByRecipientId(receipantId);
    }

    public List<Notice> getAllNoticeBySender(String senderId) {
        return noticeRepository.findAllBySenderId(senderId);
    }

    public Notice get(String noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(MyException::noObject);
    }
}
