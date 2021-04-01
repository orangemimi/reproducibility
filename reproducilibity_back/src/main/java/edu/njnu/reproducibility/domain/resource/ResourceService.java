package edu.njnu.reproducibility.domain.resource;



import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.data.DataItemRepository;
import edu.njnu.reproducibility.domain.data.support.DataItem;
import edu.njnu.reproducibility.domain.resource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceModelDTO;
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
    DataItemRepository dataItemRepository;

    public JsonResult getResources(String userId) {
//        List<Resource> resources =resourceRepository.findAllByProjectId(userId);
        return  ResultUtils.success();
    }
    public List<DataItem> getResourcesByProjectId(String projectId) {
        Resource resources =resourceRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);
        List<DataItem> dataItemList =new ArrayList<>();
        for(int i=0;i<resources.getDataItemCollection().size();i++){
            String dataId = resources.getDataItemCollection().get(i);
            DataItem dataItem = dataItemRepository.findById(dataId).orElseThrow(MyException::noObject);
            dataItemList.add(dataItem);
        }

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put()
        return  dataItemList;
    }

    public Resource updateResourceData(String pid, UpdateResourceDataDTO updateResourceDataDTO) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceDataDTO.updateTo(resource);
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
