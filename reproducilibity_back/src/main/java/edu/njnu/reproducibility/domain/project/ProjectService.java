package edu.njnu.reproducibility.domain.project;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.context.ContextDefinition;
import edu.njnu.reproducibility.domain.context.ContextDefinitionService;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectMembersDTO;
import edu.njnu.reproducibility.domain.user.User;
import edu.njnu.reproducibility.domain.user.UserRepository;
import edu.njnu.reproducibility.domain.user.UserService;
import edu.njnu.reproducibility.utils.FileUtil;
import org.apache.tomcat.util.descriptor.web.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.njnu.reproducibility.utils.Utils.addProjectRole;

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

    @Autowired
    ContextDefinitionService contextDefinitionService;


    public Project get(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public List<Project> getMyProjects(String userId) {
        JSONObject userProjectInfo = userService.getUserProjectInfo(userId);
        List<String> createdProjects = (List<String>) userProjectInfo.get("createdProjects");
        List<String> joinedProjects = (List<String>) userProjectInfo.get("joinedProjects");
        List<Project> projectList = new ArrayList<>();
        for(String projectId : createdProjects) {
            projectList.add(get(projectId));
        }
        for(String projectId : joinedProjects) {
            projectList.add(get(projectId));
        }
        return projectList;
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
                User member = userService.getUserInfoById(memberIdList.get(i).getMemberId());
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

    public JSONArray getAllProjects(String userId, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        List<String> privacyList = Arrays.asList("public", "discoverable");
        User user = userService.getUserInfoById(userId);
        Page<Project> projectList = projectRepository.findByPrivacyInOrCreator(privacyList, userId, pageable);
        return addProjectRole(projectList, user);
    }

    public Project updateMembers(String userId, String projectId, Member update) {
        Project project = projectRepository.findByIdAndCreator(projectId, userId).orElseThrow(MyException::noObject);
//        if(project.getMembers().isEmpty()){
//            project.getMembers()= new ArrayList<>();
//        }
        project.getMembers().add(update);


        User user = userService.getUserInfoById(update.getMemberId());
        user.getJoinedProjects().add(project.getId());
        userService.update(user);

        return projectRepository.save(project);
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
        Project result = projectRepository.insert(project);
        User user = userService.getUserInfoById(userId);
        user.getCreatedProjects().add(result.getId());

        userService.update(user);

        return result;
    }

    public Project fork(String userId, String userName, AddProjectDTO add) {
        String forkedProjectId =add.getForkingProjectId();

        Project project = new Project();
        add.convertTo(project);
        project.setCreator(userId);

                //update user
        User user = userService.getUserInfoById(userId);
        user.getForkedProjects().add(project.getId());
        userService.update(user);
        //update forked project
        Project projectForked = projectRepository.findById(forkedProjectId).orElse(null);
        List<String> forkedProjectIdList= new ArrayList<>();
        forkedProjectIdList.add(project.getId());
        projectForked.setForkedProjectIdList(forkedProjectIdList);
        projectRepository.save(projectForked);

//        ContextDefinition contextDefinition = contextDefinitionService.getContextDefinition(forkedProjectId);



        //add a new project
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

    public Project Star(String projectId, String userId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        int count = project.getStarCount();
        project.setStarCount(count + 1);

        userService.star(userId, projectId);
        return projectRepository.save(project);
    }

    public Project unStar(String projectId, String userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(MyException::noObject);
        project.setStarCount(project.getStarCount() - 1);
        userService.unStar(userId, projectId);
        return projectRepository.save(project);
    }

    public int getStarCount(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(MyException::noObject);
        return project.getStarCount();
    }



}
