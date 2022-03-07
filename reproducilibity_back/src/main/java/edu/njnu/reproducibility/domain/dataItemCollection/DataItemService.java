package edu.njnu.reproducibility.domain.dataItemCollection;


import com.alibaba.fastjson.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.dataItemCollection.dto.UpdateDataItemDTO;
import edu.njnu.reproducibility.domain.file.FileItem;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.remote.DataContainerService;
import edu.njnu.reproducibility.utils.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    FileItemRepository fileItemRepository;

    @Autowired
    DataContainerService dataContainerService;


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


    /**
    * @Description:保存用户文件夹下（即已经上传）的DataItem
    * @Author: Yiming
    * @Date: 2021/11/24
    */
    public String saveDataItemOfUploaded(AddDataItemDTO addDataItemDTO, JSONObject jsonObject, String userId) {
        String id = jsonObject.getString("id");
        String storey = jsonObject.getString("storey");
        FileItem fileItem = fileItemRepository.findByStoreyAndUploaderIdAndId(storey, userId, id);
        addDataItemDTO.setUserId(userId);
        addDataItemDTO.setValue(fileItem.getAddress());
        addDataItemDTO.setSize(fileItem.getFileSize());
        DataItemCollection dataItemCollection = new DataItemCollection();
        addDataItemDTO.convertTo(dataItemCollection);
        return dataItemRepository.save(dataItemCollection).getId();
    }

    /**
    * @Description:修改为用户文件夹下（即已经上传）的DataItem
    * @Author: Yiming
    * @Date: 2022/3/3
    */

    public int updateDataItemOfUploaded(UpdateDataItemDTO updateDataItemDTO, JSONObject jsonObject, String userId, String dataId) {
        String id = jsonObject.getString("id");
        String storey = jsonObject.getString("storey");
        FileItem fileItem = fileItemRepository.findByStoreyAndUploaderIdAndId(storey, userId, id);
        DataItemCollection dataItemCollection = dataItemRepository.findById(dataId).orElseThrow(MyException::noObject);
        updateDataItemDTO.convertTo(dataItemCollection);
        dataItemCollection.setValue(fileItem.getAddress());
        dataItemRepository.save(dataItemCollection);
        return 1;
    }

    /**
    * @Description:保存用户文件夹下没有的（即没有上传的）DataItem
    * @Author: Yiming
    * @Date: 2021/11/24
    */
    public String saveDataItemOfNoUpload(MultipartFile multipartFile, String name, String description, String userId, String format, String type, String projectId) throws IOException {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        String suffix="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        multipartFile.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        multiValueMap.add("datafile", resource);
        multiValueMap.add("name", name);
        String address = dataContainerService.upload(multiValueMap).getStr("id");
        AddDataItemDTO addDataItemDTO = new AddDataItemDTO();
        addDataItemDTO.setName(name);
        addDataItemDTO.setDescription(description);
        addDataItemDTO.setUserId(userId);
        addDataItemDTO.setFormat(format);
        addDataItemDTO.setType(type);
        addDataItemDTO.setProjectId(projectId);
        addDataItemDTO.setValue("http://221.226.60.2:8082/data/" + address);
        DataItemCollection dataItemCollection = new DataItemCollection();
        addDataItemDTO.convertTo(dataItemCollection);
        return dataItemRepository.save(dataItemCollection).getId();
    }

    /**
    * @Description:修改为用户文件夹下没有的（即没有上传的）DataItem
    * @Author: Yiming
    * @Date: 2022/3/3
    */

    public int updateDataItemOfNoUpload(MultipartFile multipartFile, String name, String format, String description, String type, String dataId) throws IOException {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        String suffix="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        multipartFile.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        multiValueMap.add("datafile", resource);
        multiValueMap.add("name", name);
        String address = dataContainerService.upload(multiValueMap).getStr("id");
        DataItemCollection dataItemCollection = dataItemRepository.findById(dataId).orElseThrow(MyException::noObject);
        dataItemCollection.setName(name);
        dataItemCollection.setFormat(format);
        dataItemCollection.setDescription(description);
        dataItemCollection.setType(type);
        dataItemCollection.setValue("http://221.226.60.2:8082/data/" + address);
        dataItemRepository.save(dataItemCollection);
        return 1;
    }


    /**
    * @Description:批量删除
    * @Author: Yiming
    * @Date: 2021/11/25
    */
    public void batchDelete(List<String> Ids) {
        for(String id : Ids) {
            try {
                dataItemRepository.deleteById(id);
            } catch (Exception e) {
                System.out.println(e);
                throw MyException.noObject();
            }
        }
    }

}
