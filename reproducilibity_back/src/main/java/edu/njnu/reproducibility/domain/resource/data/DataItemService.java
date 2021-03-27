package edu.njnu.reproducibility.domain.resource.data;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.resource.data.dto.AddDataItemDTO;
import edu.njnu.reproducibility.domain.resource.data.dto.UpdateDataItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//    public JsonResult getDataItemBystep(String stepId) {
//        List<DataItem> dataItemList=dataItemRepository.findAllByStepBindId(stepId).orElseThrow(MyException::noObject);
//        return ResultUtils.success(dataItemList);
//    }
    public JsonResult getAll(String pid) {
        List<DataItem> dataItemList=dataItemRepository.findAllByProjectId(pid).orElseThrow(MyException::noObject);
        return ResultUtils.success(dataItemList);
    }

    public JsonResult update(String id, UpdateDataItemDTO updateDataItemDTO) {
        DataItem dataItem=dataItemRepository.findById(id).orElseThrow(MyException::noObject);
        updateDataItemDTO.updateTo(dataItem);
        return ResultUtils.success(dataItemRepository.save(dataItem));
    }

    public JsonResult save(AddDataItemDTO add,String userId) {
        DataItem dataItem = new DataItem();
        add.convertTo(dataItem);
        dataItem.setUploaderId(userId);

        return ResultUtils.success(dataItemRepository.insert(dataItem));
    }

    public void del(String id) {
        dataItemRepository.deleteById(id);
    }


}
