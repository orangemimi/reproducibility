package edu.njnu.reproducibility.domain.userFile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/22/13:22
 * @Description:
 */
public interface UserFileRepository extends MongoRepository<UserFile, String> {
    List<UserFile> findAllByStoreyAndUserId(String storey, String userId);
    List<UserFile> findAllByStoreyAndFidAndUserId(String storey, String fid, String userId);
}
