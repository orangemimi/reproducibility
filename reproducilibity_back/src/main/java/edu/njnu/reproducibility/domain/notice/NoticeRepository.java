package edu.njnu.reproducibility.domain.notice;

import edu.njnu.reproducibility.domain.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:44
 * @modified By：
 * @version: 1.0.0
 */
@Repository
public interface NoticeRepository extends MongoRepository<Notice, String> {
    Optional<Notice> findById(String id);
    List<Notice> findAllByRecipientId(String recipientId);
    List<Notice> findAllBySenderId(String senderId);
    Optional<Notice> findByIdAndRecipientId(String recipentId,String recipientId);

}
