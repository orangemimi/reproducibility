package edu.njnu.reproducibility.domain.projectResource;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.domain.file.FileItem;
import edu.njnu.reproducibility.domain.projectResource.dto.AddProjectResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceRelatedDataDTO;
import edu.njnu.reproducibility.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:25
 * @Version 1.0.0
 */
@Service
public class ProjectResourceService {
    @Autowired
    private ProjectResourceRepository projectResourceRepository;

    @Autowired
    FileItemRepository fileItemRepository;

    public JsonResult getResources(String userId) {
//        List<Resource> resources =resourceRepository.findAllByProjectId(userId);
        return ResultUtils.success();
    }

    public List<FileItem> getResourcesByProjectId(String projectId) {
        ProjectResource resources = projectResourceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        List<FileItem> fileItemList = new ArrayList<>();
        if (resources.getDataItemCollection()==null) {
            return fileItemList;
        }

        for (int i = 0; i < resources.getDataItemCollection().size(); i++) {
            String dataId = resources.getDataItemCollection().get(i);
            FileItem fileItem = fileItemRepository.findById(dataId).orElseThrow(MyException::noObject);
            fileItemList.add(fileItem);
        }

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put()
        return fileItemList;
    }

    public ProjectResource updateResourceData(String pid, UpdateProjectResourceDataDTO updateProjectResourceDataDTO) {
        ProjectResource projectResource = projectResourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateProjectResourceDataDTO.updateTo(projectResource);
        return projectResourceRepository.save(projectResource);
    }

    public ProjectResource updateResourceRelatedData(String pid, UpdateProjectResourceRelatedDataDTO update) {
        ProjectResource projectResource = projectResourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        update.updateTo(projectResource);
        return projectResourceRepository.save(projectResource);
    }

    public ProjectResource updateResourceModel(String pid, UpdateProjectResourceModelDTO updateProjectResourceModelDTO) {
        ProjectResource projectResource = projectResourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateProjectResourceModelDTO.updateTo(projectResource);
        return projectResourceRepository.save(projectResource);
    }


    public ProjectResource saveResources(AddProjectResourceDTO add, String userId) {
        ProjectResource projectResource = new ProjectResource();
        add.convertTo(projectResource);
        projectResource.setUserId(userId);
        return projectResourceRepository.insert(projectResource);
    }

    public Object uploadPicture(MultipartFile upload, String userId) {
        //这里获取的是项目的根路径，直接加上了static/imgupload/下就找到图片，找不到，就直接去电脑文件夹找
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + userId + "/resourcePicture/";
        try {
            return FileUtil.uploadFile(upload, filePath);
        } catch (Exception e) {
            throw new MyException("Upload Failed");
        }
    }


}
