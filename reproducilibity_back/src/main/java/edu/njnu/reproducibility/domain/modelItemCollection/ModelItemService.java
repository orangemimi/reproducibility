package edu.njnu.reproducibility.domain.modelItemCollection;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.modelItemCollection.dto.AddModelItemDTO;
import edu.njnu.reproducibility.remote.RemotePortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    public Object getModelsByProvider(String userId, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<ModelItemColletion> modelItemList = modelItemRepository.findAllByUserId(userId, pageable);
        return modelItemList;
    }

//    public void del(String tid) {
//        modelItemRepository.deleteByTid(tid);
//    }

    public void saveToProject(JSONObject jsonObject, String userId) {
        AddModelItemDTO addModelItemDTO = new AddModelItemDTO();
        addModelItemDTO.setUploaderId(userId);
        addModelItemDTO.setProjectId(jsonObject.getStr("projectId"));
        addModelItemDTO.setDoi(jsonObject.getStr("doi"));
        addModelItemDTO.setMd5(jsonObject.getStr("md5"));
        String image;
        if(jsonObject.getStr("image").equals("")) {
            image = "http://geomodeling.njnu.edu.cn/static/img/model/model.png";
        } else {
            image = "http://geomodeling.njnu.edu.cn" + jsonObject.getStr("image");
        }
        addModelItemDTO.setImage(image);
        addModelItemDTO.setName(jsonObject.getStr("name"));
        addModelItemDTO.setType("service");
        addModelItemDTO.setPrivacy("public");
        addModelItemDTO.setTags(new ArrayList<>());
        ModelItemColletion modelItemColletion = new ModelItemColletion();
        addModelItemDTO.convertTo(modelItemColletion);
        modelItemRepository.save(modelItemColletion);
    }

    public void saveModelsToProject(JSONArray jsonArray, String projectId, String userId) {
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            jsonObject.put("projectId", projectId);
            saveToProject(jsonObject, userId);
        }
    }

    public List<ModelItemColletion> getModelsByProjectId(String projectId) {
        List<ModelItemColletion> list = modelItemRepository.findAllByProjectId(projectId);
        if(list == null) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }



}
