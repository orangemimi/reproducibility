package edu.njnu.reproducibility.domain.dataService;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.dataService.dto.AddDataServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */

@Service
public class DataServiceService {
    @Autowired
    DataServiceRepositroy dataServiceRepositroy;

    public Page<DataService> getAll(String userId, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<DataService> dataServiceList = dataServiceRepositroy.findAll( pageable);
        return dataServiceList;
    }

    public DataService save(String userId ,AddDataServiceDTO add) {
        DataService dataService = new DataService();

        add.convertTo(dataService);
        dataService.setUserId(userId);
        return dataServiceRepositroy.insert(dataService);
    }

    public List<DataService> getMyDataService(String userId) {
        try{
            List<DataService> allByUserId = dataServiceRepositroy.findAllByUserId(userId);
            return allByUserId;
        }catch (Exception e){
            throw new MyException(ResultEnum.NO_OBJECT);
        }

    }

    public void saveDataServiceToProject(String name, String serviceId, String token, String userId, String projectId, String oid) {
        AddDataServiceDTO addDataServiceDTO = new AddDataServiceDTO();
        addDataServiceDTO.setName(name);
        addDataServiceDTO.setDataServiceId(serviceId);
        addDataServiceDTO.setToken(token);
        addDataServiceDTO.setPrivacy("public");
        addDataServiceDTO.setUserId(userId);
        addDataServiceDTO.setProjectId(projectId);
        addDataServiceDTO.setOid(oid);
        DataService dataService = new DataService();
        addDataServiceDTO.convertTo(dataService);
        dataServiceRepositroy.insert(dataService);
    }

    public void saveDataServicesToProject(JSONArray jsonArray, String projectId, String userId) {
        for(int i = 0;i < jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            saveDataServiceToProject(jsonObject.getStr("name"), jsonObject.getStr("serviceId"), jsonObject.getStr("token"), userId, projectId, jsonObject.getStr("oid"));
        }
    }

    public List<DataService> getAllByProjectId(String projectId) {
        return dataServiceRepositroy.findAllByProjectId(projectId);
    }
}
