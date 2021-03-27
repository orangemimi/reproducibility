package edu.njnu.reproducibility.domain.resource.model;



import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.model.dto.AddModelItemDTO;
import edu.njnu.reproducibility.domain.resource.tool.ToolItem;
import edu.njnu.reproducibility.domain.resource.tool.dto.AddToolItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public JsonResult save(AddModelItemDTO add,String id) {
        ModelItem modelItem = new ModelItem();
        add.convertTo(modelItem);
        modelItem.setUploaderId(id);
        return ResultUtils.success(modelItemRepository.insert(modelItem));
    }

    public List<ModelItem> getAll(String userId) {
        List<ModelItem> modelItemList = modelItemRepository.findAllByUploaderIdOrPrivacy(userId,"public").orElseThrow(MyException::noObject);
        return modelItemList;
    }

//    public void del(String tid) {
//        modelItemRepository.deleteByTid(tid);
//    }
}
