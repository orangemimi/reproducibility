package edu.njnu.reproducibility.domain.dataItemCollection;


import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.UpdateDataItemDTO;
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
public class DataItemService {
    @Autowired
    private DataItemRepository dataItemRepository;


    public JsonResult getResources(String userId) {
//        List<Resource> resources =resourceRepository.findAllByProjectId(userId);
        return ResultUtils.success();
    }

    public List<DataItemCollection> getResourcesByProjectId(String projectId) {
        List<DataItemCollection> dataItemCollections = dataItemRepository.findAllByProjectId(projectId);
        return dataItemCollections;
    }

    public DataItemCollection updateResourceData(String id, UpdateDataItemDTO update) {
        DataItemCollection dataItemCollection = dataItemRepository.findFirstById(id).orElseThrow(MyException::noObject);
        update.updateTo(dataItemCollection);
        return dataItemRepository.save(dataItemCollection);
    }
//
//    public Resource updateResourceRelatedData(String pid, UpdateResourceRelatedDataDTO update) {
//        Resource resource = dataItemRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
//        update.updateTo(resource);
//        return dataItemRepository.save(resource);
//    }
//
//    public Resource updateResourceModel(String pid, UpdateResourceModelDTO updateResourceModelDTO) {
//        Resource resource = dataItemRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
//        updateResourceModelDTO.updateTo(resource);
//        return dataItemRepository.save(resource);
//    }


    public DataItemCollection saveResources(AddDataItemDTO add, String userId) {
        DataItemCollection dataItemCollection = new DataItemCollection();
        add.convertTo(dataItemCollection);
        dataItemCollection.setUserId(userId);
        return dataItemRepository.insert(dataItemCollection);
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

    public int delDataItem(String ID) {
        try{
            dataItemRepository.deleteById(ID);
            return 0;
        }catch (Exception e){
            throw new MyException(ResultEnum.NO_OBJECT);
        }
    }
}
