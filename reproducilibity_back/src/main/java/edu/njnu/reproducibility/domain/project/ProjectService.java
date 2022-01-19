package edu.njnu.reproducibility.domain.project;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.content.Content;
import edu.njnu.reproducibility.domain.content.ContentRepository;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.Context;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.EssentialInformation;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.SpatialInfo;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.TemporalInfo;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Resource;
import edu.njnu.reproducibility.domain.content.support.dto.AddContentDTO;
import edu.njnu.reproducibility.domain.context.ContextDefinition;
import edu.njnu.reproducibility.domain.context.ContextDefinitionService;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.dataItemCollection.DataItemRepository;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.dataService.DataService;
import edu.njnu.reproducibility.domain.dataService.DataServiceRepositroy;
import edu.njnu.reproducibility.domain.dataService.dto.AddDataServiceDTO;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTask;
import edu.njnu.reproducibility.domain.integratetask.IntegrateTaskService;
import edu.njnu.reproducibility.domain.integratetask.dto.AddIntegrateTaskDTO;
import edu.njnu.reproducibility.domain.modelItemCollection.ModelItemColletion;
import edu.njnu.reproducibility.domain.modelItemCollection.ModelItemRepository;
import edu.njnu.reproducibility.domain.modelItemCollection.dto.AddModelItemDTO;
import edu.njnu.reproducibility.domain.performance.PerformanceService;
import edu.njnu.reproducibility.domain.performance.dto.AddPerformanceDTO;
import edu.njnu.reproducibility.domain.project.dto.AddProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectDTO;
import edu.njnu.reproducibility.domain.project.dto.UpdateProjectMembersDTO;
import edu.njnu.reproducibility.domain.project.support.Record;
import edu.njnu.reproducibility.domain.scenario.Scenario;
import edu.njnu.reproducibility.domain.scenario.ScenarioRepository;
import edu.njnu.reproducibility.domain.scenario.dto.AddScenarioDTO;
import edu.njnu.reproducibility.domain.user.User;
import edu.njnu.reproducibility.domain.user.UserRepository;
import edu.njnu.reproducibility.domain.user.UserService;
import edu.njnu.reproducibility.utils.FileUtil;
import org.apache.tomcat.util.descriptor.web.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    DataItemRepository dataItemRepository;

    @Autowired
    ModelItemRepository modelItemRepository;

    @Autowired
    DataServiceRepositroy dataServiceRepositroy;

    @Autowired
    PerformanceService performanceService;

    @Autowired
    IntegrateTaskService integrateTaskService;

    @Autowired
    ScenarioRepository scenarioRepository;



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
    public JSONArray getProjectsCreatedByMe(String userId) {
        JSONObject userProjectInfo = userService.getUserProjectInfo(userId);
        List<String> createdProjects = (List<String>) userProjectInfo.get("createdProjects");
        JSONArray jsonArray = new JSONArray();
        for(String s : createdProjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("projectId", s);
            jsonObject.put("name", projectRepository.findById(s).orElseThrow(MyException::noObject).getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
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

    public Project create(String userId, String userName, AddProjectDTO add, AddPerformanceDTO addPerformanceDTO) {
        Project project = new Project();
        add.setCreator(userId);
        add.convertTo(project);
//        Creator creator = new Creator();
//        creator.setName(userName);
//        creator.setId(userId);
        Project result = projectRepository.insert(project);
        User user = userService.getUserInfoById(userId);
        user.getCreatedProjects().add(result.getId());
        userService.update(user);

        AddContentDTO addContentDTO = new AddContentDTO();
        Context context = new Context();
        Resource resource = new Resource();
        context.setEssentialInformation(new EssentialInformation(add.getName(), "", "", "", new ArrayList<>()));
        context.setSpatialInfos(new ArrayList<>());
        context.setTemporalInfo(new TemporalInfo(null, null, "", ""));
        addContentDTO.setContext(context);
        addContentDTO.setProjectId(result.getId());
        addContentDTO.setResource(resource);
        Content content = new Content();
        addContentDTO.convertTo(content);
        contentRepository.insert(content);

        addPerformanceDTO.setProjectId(result.getId());
        performanceService.savePerformance(addPerformanceDTO, userId);

        return result;

    }

    /**
    * @Description:fork项目
    * @Author: Yiming
    * @Date: 2022/1/17
    */

    public String fork(JSONObject jsonObject, String userId, String userName) {
        String name = jsonObject.getStr("name");
        String purpose = jsonObject.getStr("purpose");
        String description = jsonObject.getStr("description");
        List<String> tags = jsonObject.getJSONArray("tags").toList(String.class);
        String privacy = jsonObject.getStr("privacy");
        String forkingProjectId = jsonObject.getStr("forkingProjectId");
        AddPerformanceDTO addPerformanceDTO = jsonObject.getJSONObject("completionJson").toBean(AddPerformanceDTO.class);
        EssentialInformation essentialInformation = jsonObject.getJSONObject("essentialInformation").toBean(EssentialInformation.class);
        List<SpatialInfo> spatialInfos = jsonObject.getJSONArray("spatialInfos").toList(SpatialInfo.class);
        TemporalInfo temporalInfo = jsonObject.getJSONObject("temporalInfo").toBean(TemporalInfo.class);
        JSONArray dataItemCollections = jsonObject.getJSONArray("resources");
        AddProjectDTO addProjectDTO = new AddProjectDTO();
        addProjectDTO.setCreator(userId);
        addProjectDTO.setName(name);
        addProjectDTO.setPurpose(purpose);
        addProjectDTO.setDescription(description);
        addProjectDTO.setTags(tags);
        addProjectDTO.setPrivacy(privacy);
        addProjectDTO.setForkingProjectId(forkingProjectId);
        addProjectDTO.setStarCount(0);
        addProjectDTO.setWatchCount(0);
        addProjectDTO.setFolkCount(0);
        Project project = new Project();
        project.setForkRecordId(projectRepository.findById(forkingProjectId).orElseThrow(MyException::noObject).getRecords().size());
        addProjectDTO.convertTo(project);
        projectRepository.insert(project);

        //User相关
        User user = userService.getUserInfoById(userId);
        user.getCreatedProjects().add(project.getId());

        //IntegrateTask相关
        AddIntegrateTaskDTO addIntegrateTaskDTO = new AddIntegrateTaskDTO();
        addIntegrateTaskDTO.setProjectId(project.getId());
        addIntegrateTaskDTO.setCreator(userId);
        addIntegrateTaskDTO.setUserName(userName);
        addIntegrateTaskDTO.setTaskName(name);
        addIntegrateTaskDTO.setTaskDescription("");
        addIntegrateTaskDTO.setTaskContent("<mxGraphModel>\n" +
                "  <root>\n" +
                "    <mxCell id=\"0\" />\n" +
                "    <mxCell id=\"1\" parent=\"0\" />\n" +
                "  </root>\n" +
                "</mxGraphModel>\n");
        addIntegrateTaskDTO.setSelectInstanceId("");
        IntegrateTask integrateTask = integrateTaskService.saveModelTaskInfo(addIntegrateTaskDTO, userId);

        //Scenario相关
        AddScenarioDTO addScenarioDTO = new AddScenarioDTO();
        addScenarioDTO.setProjectId(project.getId());
        addScenarioDTO.setUserId(userId);
        Scenario scenario = new Scenario();
        addScenarioDTO.convertTo(scenario);
        scenario.setType("integrateTask");
        scenario.setSelectTaskId(integrateTask.getId());
        scenarioRepository.insert(scenario);

        addPerformanceDTO.setProjectId(project.getId());
        performanceService.savePerformance(addPerformanceDTO, userId);
        AddContentDTO addContentDTO = new AddContentDTO();
        addContentDTO.setContext(new Context());
        addContentDTO.setResource(new Resource());
        addContentDTO.setProjectId(project.getId());
        Project forkProject = projectRepository.findById(forkingProjectId).orElseThrow(MyException::noObject);
        forkProject.setFolkCount(forkProject.getFolkCount() + 1);
        List<String> temp = forkProject.getForkedProjectIdList();
        temp.add(project.getId());
        forkProject.setForkedProjectIdList(temp);
        projectRepository.save(forkProject);
        Context context = new Context();
        context.setEssentialInformation(essentialInformation);
        context.setSpatialInfos(spatialInfos);
        context.setTemporalInfo(temporalInfo);

        addContentDTO.setContext(context);
        Content content = new Content();
        addContentDTO.convertTo(content);
        contentRepository.insert(content);
        for(int i = 0; i < dataItemCollections.size(); i++) {
            if(dataItemCollections.get(i, JSONObject.class).getStr("type").equals("modelService")) {
                AddModelItemDTO addModelItemDTO = new AddModelItemDTO();
                addModelItemDTO.setName(dataItemCollections.get(i, JSONObject.class).getStr("label"));
                addModelItemDTO.setDoi(dataItemCollections.get(i, JSONObject.class).getStr("doi"));
                addModelItemDTO.setMd5(dataItemCollections.get(i, JSONObject.class).getStr("md5"));
                addModelItemDTO.setPrivacy("public");
                addModelItemDTO.setType("service");
                addModelItemDTO.setProjectId(project.getId());
                ModelItemColletion modelItemColletion = new ModelItemColletion();
                addModelItemDTO.convertTo(modelItemColletion);
                modelItemRepository.insert(modelItemColletion);
            } else if (dataItemCollections.get(i, JSONObject.class).getStr("type").equals("dataService")) {
                AddDataServiceDTO addDataServiceDTO = new AddDataServiceDTO();
                addDataServiceDTO.setName(dataItemCollections.get(i, JSONObject.class).getStr("name"));
                addDataServiceDTO.setDataServiceId(dataItemCollections.get(i, JSONObject.class).getStr("dataServiceId"));
                addDataServiceDTO.setToken(dataItemCollections.get(i, JSONObject.class).getStr("token"));
                addDataServiceDTO.setOid(dataItemCollections.get(i, JSONObject.class).getStr("oid"));
                addDataServiceDTO.setPrivacy("public");
                addDataServiceDTO.setProjectId(project.getId());
                addDataServiceDTO.setUserId(userId);
                DataService dataService = new DataService();
                addDataServiceDTO.convertTo(dataService);
                dataServiceRepositroy.insert(dataService);
            } else {
                AddDataItemDTO addDataItemDTO = new AddDataItemDTO();
                addDataItemDTO.setName(dataItemCollections.get(i, JSONObject.class).getStr("label"));
                addDataItemDTO.setType(dataItemCollections.get(i, JSONObject.class).getStr("type"));
                addDataItemDTO.setFormat(dataItemCollections.get(i, JSONObject.class).getStr("type").equals("input") ? "FILE" : "STRING");
                addDataItemDTO.setUserId(userId);
                addDataItemDTO.setProjectId(project.getId());
                addDataItemDTO.setValue(dataItemCollections.get(i, JSONObject.class).getStr("value"));
                DataItemCollection dataItem = new DataItemCollection();
                addDataItemDTO.convertTo(dataItem);
                dataItemRepository.insert(dataItem);
            }
        }
        return project.getId();
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

    public String getPrivacyOfProject(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(MyException::noObject);
        return project.getPrivacy();
    }

    /**
    * @Description:分页查询projects
    * @Author: Yiming
    * @Date: 2022/1/10
    */
    public Page<Project> getProjectsPage(int currentPage, int pageSize) {
        PageRequest pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Project> projects = projectRepository.findByPrivacy("public", pageable);
        return projects;
    }

    /**
    * @Description:记录record
    * @Author: Yiming
    * @Date: 2022/1/17
    */
    public void saveRecord(Record record, String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(MyException::noObject);
        record.setUpdateDate(new Date());
        List<Record> records = project.getRecords();
        records.add(record);
        project.setRecords(records);
        projectRepository.save(project);
    }
    /**
    * @Description:获取所有record
    * @Author: Yiming
    * @Date: 2022/1/18
    */
    public List<Record> getRecords(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(MyException::noObject);
        String forkProjectId = project.getForkingProjectId();
        int recordId = project.getForkRecordId();
        Project forkProject = projectRepository.findById(forkProjectId).orElseThrow(MyException::noObject);
        List<Record> records = new ArrayList<>();
        for(int i = recordId - 1; i < forkProject.getRecords().size(); i++) {
            records.add(forkProject.getRecords().get(i));
        }
        return records;
    }



}
