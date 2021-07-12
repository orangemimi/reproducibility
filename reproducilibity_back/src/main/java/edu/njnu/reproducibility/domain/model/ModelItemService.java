package edu.njnu.reproducibility.domain.model;



import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
import edu.njnu.reproducibility.domain.modelItemCollection.dto.AddModelItemDTO;
=======
import edu.njnu.reproducibility.domain.model.dto.AddModelItemDTO;
import edu.njnu.reproducibility.domain.model.support.ModelItem;
>>>>>>> parent of 4c1cfb9 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
import edu.njnu.reproducibility.remote.RemotePortalService;
=======
import edu.njnu.reproducibility.domain.model.dto.AddModelItemDTO;
import edu.njnu.reproducibility.domain.model.support.ModelItem;
>>>>>>> parent of f11cd19 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/10/14 14:42
 * @modified By：
 * @version:     1.0.0
 */
@Service
public class ModelItemService {
    @Autowired
    private ModelItemRepository modelItemRepository;

//    public JsonResult getAll(String pid) {
//        List<ModelItem> modelItemList = modelItemRepository.findAllByProjectId(pid).orElseThrow(MyException::noObject);
//        return ResultUtils.success(modelItemList);
//    }

//    public JsonResult update(String id, UpdateDataItemDTO updateDataItemDTO) {
//        ModelItem modelItem=modelItemRepository.findById(id).orElseThrow(MyException::noObject);
//        updateDataItemDTO.updateTo(modelItem);
//        return ResultUtils.success(modelItemRepository.save(modelItem));
//    }

<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
    public JsonResult save(AddModelItemDTO add, String id) {
        ModelItem modelItem = new ModelItem();
        add.convertTo(modelItem);
        modelItem.setUserId(id);
        JSONObject data = remotePortalService.getModelInfo(add.getDoi());
        modelItem.setMd5(data.getStr("md5"));


        return ResultUtils.success(modelItemRepository.insert(modelItem));
    }

    public Page<ModelItem> getAll(String userId, int currentPage, int pagesize) {
//        PageRequest pageable =  PageRequest.of(currentPage, pagesize, Sort.Direction.DESC);
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<ModelItem> modelItemList = modelItemRepository.findAllByUserIdOrPrivacy(userId, "public", pageable);
        return modelItemList;
    }

    public Page<ModelItem> getPublicModels(String privacy, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByPrivacy(privacy, pageable);
=======
    public JsonResult save(AddModelItemDTO add,String id) {
        ModelItem modelItem = new ModelItem();
        add.convertTo(modelItem);
        modelItem.setUploaderId(id);
        return ResultUtils.success(modelItemRepository.insert(modelItem));
    }

    public Page<ModelItem> getAll(String userId,int currentPage,int pagesize) {
//        PageRequest pageable =  PageRequest.of(currentPage, pagesize, Sort.Direction.DESC);
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        Page<ModelItem> modelItemList = modelItemRepository.findAllByUploaderIdOrPrivacy(userId,"public",pageable);
        return modelItemList;
    }

    public Page<ModelItem> getPublicModels(String privacy,int currentPage, int pagesize) {
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        Page<ModelItem> modelItemList = modelItemRepository.findAllByPrivacy(privacy,pageable);
>>>>>>> parent of f11cd19 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
=======
        Page<ModelItem> modelItemList = modelItemRepository.findAllByPrivacy(privacy, pageable);
>>>>>>> parent of 4c1cfb9 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
        return modelItemList;
    }

    public Object getModelsByProvider(String userId, String privacy, int currentPage, int pagesize) {
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
<<<<<<< HEAD:reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/modelItemCollection/ModelItemService.java
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByUserId(userId, pageable);
=======
        PageRequest pageable =  PageRequest.of(currentPage, pagesize);
        Page<ModelItem> modelItemList = modelItemRepository.findAllByUploaderId(userId,pageable);
>>>>>>> parent of f11cd19 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
=======
        Page<ModelItem> modelItemList = modelItemRepository.findAllByUserId(userId, pageable);
>>>>>>> parent of 4c1cfb9 (mxgraph):reproducilibity_back/src/main/java/edu/njnu/reproducibility/domain/model/ModelItemService.java
        return modelItemList;
    }

//    public void del(String tid) {
//        modelItemRepository.deleteByTid(tid);
//    }
}
