package edu.njnu.reproducibility.domain.project;


import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.utils.FileUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:44
 * @modified By：
 * @version: 1.0.0
 */

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public Project get(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public List<Project> getAllProjects() {
        return Lists.newArrayList(projectRepository.findAll());
//        return Arrays.asList(projectRepository.findAll());
    }

    public Object getCreatedProjectsByUser(String userId) {
        return "";
    }

    public Object getJoinedProjectsByUser(String userId) {
        return "";
    }

    public Object update(String projectId, String userId, UpdateProjectDTO update) {
        Project project = projectRepository.findByIdAndCreator(projectId, userId).orElseThrow(MyException::noObject);
        update.updateTo(project);
        return projectRepository.save(project);
    }

    public Object create(String userId, AddProjectDTO add) {
        Project project = new Project();
        add.convertTo(project);
        project.setCreator(userId);
        return projectRepository.insert(project);
    }

    public void delete(String projectId) {
        projectRepository.deleteById(projectId);
    }


    public Object uploadPicture(MultipartFile upload, String userId) {
        //这里获取的是项目的根路径，直接加上了static/imgupload/下就找到图片，找不到，就直接去电脑文件夹找
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/pictureFile/" + userId+"/";
        try {
            return FileUtil.uploadFile(upload, filePath);
        } catch (Exception e) {
            throw new MyException("Upload Failed");
        }
    }


    public Object deletePicture(String fileName, String userId) {
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/pictureFile/" + userId +"/"+ fileName;
        return FileUtil.deleteFile(filePath);
    }
}
