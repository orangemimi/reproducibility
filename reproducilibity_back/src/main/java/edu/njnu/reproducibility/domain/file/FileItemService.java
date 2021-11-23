package edu.njnu.reproducibility.domain.file;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.dto.AddFileItemDTO;
import edu.njnu.reproducibility.domain.file.dto.UpdateFileItemDTO;
import edu.njnu.reproducibility.domain.project.Member;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.project.ProjectService;
import edu.njnu.reproducibility.domain.projectResource.ProjectResourceService;
import edu.njnu.reproducibility.domain.userFile.UserFile;
import edu.njnu.reproducibility.remote.DataContainerService;
import edu.njnu.reproducibility.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:25
 * @Version 1.0.0
 */
@Service
public class FileItemService {
    @Autowired
    private FileItemRepository fileItemRepository;
    //    public JsonResult getDataItemBystep(String stepId) {
//        List<DataItem> dataItemList=dataItemRepository.findAllByStepBindId(stepId).orElseThrow(MyException::noObject);
//        return ResultUtils.success(dataItemList);
//    }
    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectResourceService projectResourceService;

    @Autowired
    DataContainerService dataContainerService;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime;

    public List<FileItem> getAll(String userId) {
        List<FileItem> fileItemList = fileItemRepository.findAllByUploaderId(userId);
        return fileItemList;
    }


    public List<FileItem> getByCreatorId(String userId, String projectId) {
        Optional<Project> project = Optional.ofNullable(projectService.get(projectId));

        List<Member> memberList = project.get().getMembers();
        List<FileItem> fileItemList =new ArrayList<>();
        for(Member member:memberList){
            //如果是其中一个的化
            if(member.getMemberId().equals(userId)){
                fileItemList = projectResourceService.getResourcesByProjectId(projectId);
            }
        }

        return fileItemList;
    }

    public JsonResult update(String id, UpdateFileItemDTO updateFileItemDTO) {
        FileItem fileItem = fileItemRepository.findById(id).orElseThrow(MyException::noObject);
        updateFileItemDTO.updateTo(fileItem);
        return ResultUtils.success(fileItemRepository.save(fileItem));
    }

    //新建文件夹用这个方法
    public FileItem save(AddFileItemDTO add, String userId) {
        String parent = add.getParent();
        String storey = String.valueOf(Integer.parseInt(add.getStorey()) - 1);
        if(!parent.equals("-1")) {
            FileItem father = fileItemRepository.findByStoreyAndUploaderIdAndId(storey, userId, parent);
            if(father == null) {
                throw MyException.noObject();
            }
            Date date = new Date();
            updateTime = date;
            father.setUpdateTime(updateTime);
            if(father.getSon() == null) {
                List<String> son = new ArrayList<>();
                father.setSon(son);
            }
            FileItem fileItem = new FileItem();
            add.convertTo(fileItem);
            fileItem.setUploaderId(userId);
            FileItem result = fileItemRepository.insert(fileItem);
            father.getSon().add(result.getId());
            fileItemRepository.save(father);
            return result;
        } else {
            FileItem fileItem = new FileItem();
            add.convertTo(fileItem);
            fileItem.setUploaderId(userId);
            return fileItemRepository.insert(fileItem);
        }
    }
    //新建文件用这个方法
    public FileItem addFileItem(String name, String userId, MultipartFile multipartFile, String parent, String storey) throws IOException {
        String size = Utils.getPrintSize(multipartFile.getSize());
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        String suffix="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        multipartFile.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        multiValueMap.add("datafile", resource);
        multiValueMap.add("name", name);
        String address = dataContainerService.upload(multiValueMap).getStr("id");
        String fStorey = String.valueOf(Integer.parseInt(storey) - 1);
        if(!parent.equals("-1")) {
            FileItem father = fileItemRepository.findByStoreyAndUploaderIdAndId(fStorey, userId, parent);
            if(father == null) {
                throw MyException.noObject();
            }
            Date date = new Date();
            updateTime = date;
            father.setUpdateTime(updateTime);
            if(father.getSon() == null) {
                List<String> son = new ArrayList<>();
                father.setSon(son);
            }
            FileItem fileItem = new FileItem();
            AddFileItemDTO addFileItemDTO = new AddFileItemDTO();
            if(!suffix.equals(".")) {
                name = name + suffix;
                addFileItemDTO.setSuffix(suffix);
            }
            addFileItemDTO.setName(name);
            addFileItemDTO.setAddress(address);
            addFileItemDTO.setParent(parent);
            addFileItemDTO.setStorey(storey);
            addFileItemDTO.setUploaderId(userId);
            addFileItemDTO.setFolder(false);
            addFileItemDTO.setFileSize(size);
            addFileItemDTO.convertTo(fileItem);
            FileItem result = fileItemRepository.insert(fileItem);
            father.getSon().add(result.getId());
            fileItemRepository.save(father);
            return result;
        } else {
            FileItem fileItem = new FileItem();
            AddFileItemDTO addFileItemDTO = new AddFileItemDTO();
            if(!suffix.equals(".")) {
                name = name + suffix;
                addFileItemDTO.setSuffix(suffix);
            }
            addFileItemDTO.setName(name);
            addFileItemDTO.setAddress(address);
            addFileItemDTO.setParent(parent);
            addFileItemDTO.setStorey(storey);
            addFileItemDTO.setUploaderId(userId);
            addFileItemDTO.setFolder(false);
            addFileItemDTO.setFileSize(size);
            addFileItemDTO.convertTo(fileItem);
            FileItem result = fileItemRepository.insert(fileItem);
            return result;
        }
    }

    public void del(String id) {
        fileItemRepository.deleteById(id);
    }

    public FileItem getById(String id) {
        FileItem fileItem = fileItemRepository.findById(id).orElseThrow(MyException::noObject);
        return fileItem;
    }

    public JSONArray getFileItemByStoreyAndParent(String parent, String storey, String userId) {
        JSONArray jsonArray = new JSONArray();
        List<FileItem> fileItemList;
        if(parent.equals("-1") && storey.equals("0")) {
            fileItemList = fileItemRepository.findAllByStoreyAndUploaderId("0", userId);
        } else {
            fileItemList = fileItemRepository.findAllByStoreyAndParentAndUploaderId(storey, parent, userId);
        }
        for(FileItem fileItem : fileItemList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", fileItem.getName());
            jsonObject.put("date", fileItem.getUpdateTime());
            jsonObject.put("folder", fileItem.getFolder());
            jsonObject.put("fileSize", fileItem.getFileSize());
            jsonObject.put("id", fileItem.getId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public Date Rename(String id, String name) {
        UpdateFileItemDTO updateFileItemDTO = new UpdateFileItemDTO();
        updateFileItemDTO.setName(name);
        FileItem fileItem = fileItemRepository.findById(id).orElseThrow(MyException::noObject);
        updateFileItemDTO.updateTo(fileItem);
        fileItemRepository.save(fileItem);
        return fileItem.getUpdateTime();
    }

}
