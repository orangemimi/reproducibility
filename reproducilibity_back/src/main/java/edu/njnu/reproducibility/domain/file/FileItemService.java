package edu.njnu.reproducibility.domain.file;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.dto.AddFileItemDTO;
import edu.njnu.reproducibility.domain.file.dto.UpdateFileItemDTO;
import edu.njnu.reproducibility.domain.project.Member;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.project.ProjectService;
import edu.njnu.reproducibility.domain.projectResource.ProjectResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public JsonResult save(AddFileItemDTO add, String userId) {
        FileItem fileItem = new FileItem();
        add.convertTo(fileItem);
        fileItem.setUploaderId(userId);

        return ResultUtils.success(fileItemRepository.insert(fileItem));
    }

    public void del(String id) {
        fileItemRepository.deleteById(id);
    }

    public FileItem getById(String id) {
        FileItem fileItem = fileItemRepository.findById(id).orElseThrow(MyException::noObject);
        return fileItem;
    }


}
