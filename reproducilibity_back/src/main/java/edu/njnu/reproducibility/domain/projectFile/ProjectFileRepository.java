package edu.njnu.reproducibility.domain.projectFile;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/18/13:16
 * @Description:
 */
public interface ProjectFileRepository extends MongoRepository<ProjectFile, String> {
}
