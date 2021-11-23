package edu.njnu.reproducibility.domain.file;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:41
 * @Version 1.0.0
 */
public interface FileItemRepository extends MongoRepository<FileItem,String> {
    Optional<FileItem> findById(String pid);
    List<FileItem> findAllByUploaderId(String id);
//    Optional<List<DataItem>> findAllByStepBindId(String stepId);

    List<FileItem> findAllByStoreyAndUploaderId(String storey, String uploaderId);
    List<FileItem> findAllByStoreyAndParentAndUploaderId(String storey, String parent, String uploaderId);

    FileItem findByStoreyAndUploaderIdAndId(String storey, String uploaderId, String id);
}
