package edu.njnu.reproducibility.domain.result;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.file.FileItem;
import edu.njnu.reproducibility.domain.file.FileItemRepository;
import edu.njnu.reproducibility.domain.projectResource.ProjectResource;
import edu.njnu.reproducibility.domain.projectResource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceDataDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceModelDTO;
import edu.njnu.reproducibility.domain.projectResource.dto.UpdateResourceRelatedDataDTO;
import edu.njnu.reproducibility.domain.result.dto.AddResultCollectionDTO;
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
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    FileItemRepository fileItemRepository;



    public ResultCollection getResultByProjectId(String projectId) {
        ResultCollection resultCollection = resultRepository.findByProjectId(projectId).orElseThrow(MyException::noObject);

        return resultCollection;
    }

//    public ResultCollection updateResult(String pid, UpdateResourceDataDTO updateResourceDataDTO) {
//        ResultCollection resultCollection = resultRepository.findByProjectId(pid).orElseThrow(MyException::noObject);
//        updateResourceDataDTO.updateTo(resultCollection);
//        return resultRepository.save(resultCollection);
//    }



    public ResultCollection saveResult(AddResultCollectionDTO add, String userId) {
        ResultCollection resultCollection = new ResultCollection();

        add.convertTo(resultCollection);
        resultCollection.setUserId(userId);
        return resultRepository.insert(resultCollection);
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
