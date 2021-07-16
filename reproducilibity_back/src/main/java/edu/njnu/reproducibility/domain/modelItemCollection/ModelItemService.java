package edu.njnu.reproducibility.domain.modelItemCollection;


import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.modelItemCollection.dto.AddModelItemDTO;
import edu.njnu.reproducibility.remote.RemotePortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Author ：Zhiyi
 * @Date ：2020/10/14 14:42
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class ModelItemService {
    @Autowired
    ModelItemRepository modelItemRepository;

    @Autowired
    RemotePortalService remotePortalService;

//    public JsonResult getAll(String pid) {
//        List<ModelItem> modelItemList = modelItemRepository.findAllByProjectId(pid).orElseThrow(MyException::noObject);
//        return ResultUtils.success(modelItemList);
//    }

//    public JsonResult update(String id, UpdateDataItemDTO updateDataItemDTO) {
//        ModelItem modelItem=modelItemRepository.findById(id).orElseThrow(MyException::noObject);
//        updateDataItemDTO.updateTo(modelItem);
//        return ResultUtils.success(modelItemRepository.save(modelItem));
//    }

    public JsonResult save(AddModelItemDTO add, String id) {
        ModelItemColletion modelItemColletion = new ModelItemColletion();
        add.convertTo(modelItemColletion);
        modelItemColletion.setUserId(id);
        JSONObject data = remotePortalService.getModelInfo(add.getDoi());
        modelItemColletion.setMd5(data.getStr("md5"));


        return ResultUtils.success(modelItemRepository.insert(modelItemColletion));
    }

    public Page<ModelItemColletion> getAll(String userId, int currentPage, int pagesize) {
//        PageRequest pageable =  PageRequest.of(currentPage, pagesize, Sort.Direction.DESC);
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByUserIdOrPrivacy(userId, "public", pageable);
        return modelItemList;
    }

    public Page<ModelItemColletion> getPublicModels(String privacy, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByPrivacy(privacy, pageable);
        return modelItemList;
    }

    public Object getModelsByProvider(String userId, String privacy, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByUserId(userId, pageable);
        return modelItemList;
    }

//    public void del(String tid) {
//        modelItemRepository.deleteByTid(tid);
//    }
}
