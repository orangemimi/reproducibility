package edu.njnu.reproducibility.domain.resource;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.domain.projectResource.ProjectResource;

import edu.njnu.reproducibility.domain.projectResource.dto.AddProjectResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateProjectResourceRelatedDataDTO;
import edu.njnu.reproducibility.domain.resource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceModelDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceRelatedDataDTO;
import edu.njnu.reproducibility.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:25
 * @Version 1.0.0
 */
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    FileItemRepository fileItemRepository;

    public JsonResult getResources(String userId) {
//        List<Resource> resources =resourceRepository.findAllByProjectId(userId);
        return ResultUtils.success();
    }

    public List<Resource> getResourcesByProjectId(String projectId) {
        List<Resource> resources = resourceRepository.findAllByProjectId(projectId);

        return resources;
    }

    public Resource updateResourceData(String pid, UpdateResourceDataDTO updateProjectResourceDataDTO) {
        Resource projectResource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateProjectResourceDataDTO.updateTo(projectResource);
        return resourceRepository.save(projectResource);
    }

    public Resource updateResourceRelatedData(String pid, UpdateResourceRelatedDataDTO update) {
        Resource projectResource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        update.updateTo(projectResource);
        return resourceRepository.save(projectResource);
    }

    public Resource updateResourceModel(String pid, UpdateResourceModelDTO updateProjectResourceModelDTO) {
        Resource projectResource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateProjectResourceModelDTO.updateTo(projectResource);
        return resourceRepository.save(projectResource);
    }


    public Resource saveResources(AddResourceDTO add, String userId) {
        Resource projectResource = new Resource();
        add.convertTo(projectResource);
        projectResource.setUserId(userId);
        return resourceRepository.insert(projectResource);
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
