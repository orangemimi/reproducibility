package edu.njnu.reproducibility.domain.record;


import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.record.dto.AddRecordDTO;
import edu.njnu.reproducibility.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @Author Zhiyi
 * @Date 2020/8/19  15:46
 * @Version 1.0.0
 */
@Service
public class RecordService {
    @Autowired
    RecordRepository recordRepository;

    public Optional<Record>  getModelTaskInfo(String id) {
        Optional<Record> record = recordRepository.findById(id);
        return record;
    }

    public List<Record> getAllRecords(String pid) {
        List<Record> recordList = recordRepository.findAllByProjectId(pid);
        return  recordList;
    }

    public Record saveRecord(AddRecordDTO add,String userId) {
        Record record = new Record();
        record.setUserId(userId);
        add.convertTo(record);
        return recordRepository.insert(record);
    }

    public void deleteById(String id) {
        recordRepository.deleteById(id);
    }



}
