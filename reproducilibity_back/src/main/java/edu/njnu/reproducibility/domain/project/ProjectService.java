package edu.njnu.reproducibility.domain.project;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.domain.user.User;
import edu.njnu.reproducibility.domain.user.UserService;
import edu.njnu.reproducibility.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    UserService userService;

    public Project get(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public Object getProjectAndUsers(String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        String creatorId = project.getCreator();
        User creator = userService.getUserInfoById(creatorId);
        JSONObject creatorJson = new JSONObject();
        creatorJson.put("id", creator.getUserId());
        creatorJson.put("name", creator.getName());

        List<Member> memberIdList = project.getMembers();
        List<JSONObject> memberList = new ArrayList<>();
        JSONObject json = new JSONObject();

        json.put("project", project);
        json.put("creator", creatorJson);
        if (!(memberIdList == null || memberIdList.size() == 0)) {
            for (int i = 0; i < memberIdList.size(); i++) {
                User member = userService.getUserInfoById(memberIdList.get(i).getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", member.getUserId());
                jsonObject.put("name", member.getName());
                jsonObject.put("role", memberIdList.get(i).getRole());
                memberList.add(jsonObject);
            }
            json.put("members", memberList);
        }
        return json;
    }

    public Page<Project> getAllProjects(String userId,int currentPage,int pagesize) {
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        List<String> privacyList = Arrays.asList("public", "discoverable");
        Page<Project> projectList = projectRepository.findByPrivacyInOrCreator(privacyList, userId,pageable);
        return projectList;
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

    public Project create(String userId, String userName, AddProjectDTO add) {
        Project project = new Project();
        add.convertTo(project);
//        Creator creator = new Creator();
//        creator.setName(userName);
//        creator.setId(userId);
        project.setCreator(userId);
        return projectRepository.insert(project);
    }


    public void delete(String projectId) {
        projectRepository.deleteById(projectId);
    }


    public Object uploadPicture(MultipartFile upload, String userId) {
        //这里获取的是项目的根路径，直接加上了static/imgupload/下就找到图片，找不到，就直接去电脑文件夹找
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + userId + "/projectPicture/";
        try {
            return FileUtil.uploadFile(upload, filePath);
        } catch (Exception e) {
            throw new MyException("Upload Failed");
        }
    }


    public Object deletePicture(String fileName, String userId) {
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/projectPicture/" + userId + "/" + fileName;
        return FileUtil.deleteFile(filePath);
    }



}
