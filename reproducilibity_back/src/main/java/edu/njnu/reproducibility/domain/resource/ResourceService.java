package edu.njnu.reproducibility.domain.resource;


<<<<<<< HEAD
=======

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
>>>>>>> parent of f11cd19 (mxgraph)
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.FileItem;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.domain.projectResource.Resource;
import edu.njnu.reproducibility.domain.projectResource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceRelatedDataDTO;
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
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    FileItemRepository fileItemRepository;

    public JsonResult getResources(String userId) {
//        List<Resource> resources =resourceRepository.findAllByProjectId(userId);
        return  ResultUtils.success();
    }
<<<<<<< HEAD

    public List<FileItem> getResourcesByProjectId(String projectId) {
        edu.njnu.reproducibility.domain.projectResource.Resource resources = resourceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        List<FileItem> fileItemList = new ArrayList<>();
        if (resources.getDataItemCollection()==null) {
            return fileItemList;
        }

        for (int i = 0; i < resources.getDataItemCollection().size(); i++) {
=======
    public List<DataItem> getResourcesByProjectId(String projectId) {
        Resource resources =resourceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        List<DataItem> dataItemList =new ArrayList<>();
        for(int i=0;i<resources.getDataItemCollection().size();i++){
>>>>>>> parent of f11cd19 (mxgraph)
            String dataId = resources.getDataItemCollection().get(i);
            FileItem fileItem = fileItemRepository.findById(dataId).orElseThrow(MyException::noObject);
            fileItemList.add(fileItem);
        }

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put()
<<<<<<< HEAD
        return fileItemList;
    }

    public edu.njnu.reproducibility.domain.projectResource.Resource updateResourceData(String pid, UpdateResourceDataDTO updateResourceDataDTO) {
        edu.njnu.reproducibility.domain.projectResource.Resource resource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceDataDTO.updateTo(resource);
        return resourceRepository.save(resource);
    }

    public edu.njnu.reproducibility.domain.projectResource.Resource updateResourceRelatedData(String pid, UpdateResourceRelatedDataDTO update) {
        edu.njnu.reproducibility.domain.projectResource.Resource resource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        update.updateTo(resource);
        return resourceRepository.save(resource);
    }

    public edu.njnu.reproducibility.domain.projectResource.Resource updateResourceModel(String pid, UpdateResourceModelDTO updateResourceModelDTO) {
        edu.njnu.reproducibility.domain.projectResource.Resource resource = resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceModelDTO.updateTo(resource);
        return resourceRepository.save(resource);
    }


<<<<<<< HEAD
    public ProjectResource saveResources(AddResourceDTO add, String userId) {
        ProjectResource projectResource = new ProjectResource();
        add.convertTo(projectResource);
        projectResource.setUserId(userId);
        return resourceRepository.insert(projectResource);
=======
        return  dataItemList;
    }

    public Resource updateResourceData(String pid, UpdateResourceDataDTO updateResourceDataDTO) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceDataDTO.updateTo(resource);
        return resourceRepository.save(resource);
    }

    public Resource updateResourceRelatedData(String pid, UpdateResourceRelatedDataDTO update) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        update.updateTo(resource);
        return resourceRepository.save(resource);
    }

    public Resource updateResourceModel(String pid, UpdateResourceModelDTO updateResourceModelDTO) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceModelDTO.updateTo(resource);
        return resourceRepository.save(resource);
    }


    public Resource saveResources(AddResourceDTO add,String userId) {
        Resource resource = new Resource();
        add.convertTo(resource);
        resource.setUserId(userId);
        return  resourceRepository.insert(resource);
>>>>>>> parent of f11cd19 (mxgraph)
=======
    public edu.njnu.reproducibility.domain.projectResource.Resource saveResources(AddResourceDTO add, String userId) {
        edu.njnu.reproducibility.domain.projectResource.Resource resource = new Resource();
        add.convertTo(resource);
        resource.setUserId(userId);
        return resourceRepository.insert(resource);
>>>>>>> parent of 4c1cfb9 (mxgraph)
    }

    public Object uploadPicture(MultipartFile upload, String userId) {
        //这里获取的是项目的根路径，直接加上了static/imgupload/下就找到图片，找不到，就直接去电脑文件夹找
        String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() +"static/"+userId+"/resourcePicture/";
        try {
            return FileUtil.uploadFile(upload, filePath);
        } catch (Exception e) {
            throw new MyException("Upload Failed");
        }
    }


}
