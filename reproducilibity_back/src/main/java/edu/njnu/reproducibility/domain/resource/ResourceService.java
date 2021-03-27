package edu.njnu.reproducibility.domain.resource;



import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.dto.AddResourceDTO;
import edu.njnu.reproducibility.domain.resource.dto.UpdateResourceDTO;
import edu.njnu.reproducibility.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Zhiyi
 * @Date 2020/8/13  21:25
 * @Version 1.0.0
 */
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    public JsonResult getResources(String pid) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        return  ResultUtils.success(resource);
    }

    public JsonResult updateResources(String pid, UpdateResourceDTO updateResourceDTO) {
        Resource resource =resourceRepository.findFirstByProjectId(pid).orElseThrow(MyException::noObject);
        updateResourceDTO.updateTo(resource);
        return ResultUtils.success(resourceRepository.save(resource));
    }


    public JsonResult saveResources(AddResourceDTO add) {
        Resource resource = new Resource();
        add.convertTo(resource);
        return  ResultUtils.success(resourceRepository.insert(resource));
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
