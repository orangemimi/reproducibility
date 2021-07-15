package edu.njnu.reproducibility.domain.dataServiceCode;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.dataServiceCode.dto.AddDataServiceCodeDTO;
import edu.njnu.reproducibility.domain.dataServiceCode.support.DependencyItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.InputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.OutputItem;
import edu.njnu.reproducibility.domain.dataServiceCode.support.ParameterItem;
import edu.njnu.reproducibility.remote.DataContainerService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.List;

import static edu.njnu.reproducibility.utils.FileUtil.getFile;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/16 20:13
 * @modified By：
 * @version: 1.0.0
 */

@Service
public class DataServiceCodeService {
    @Autowired
    DataServiceCodeRepositroy dataServiceCodeRepositroy;

    @Autowired
    DataContainerService dataContainerService;

    public Page<DataServiceCode> getAll(String userId, int currentPage, int pagesize) {
        PageRequest pageable = PageRequest.of(currentPage, pagesize);
        Page<DataServiceCode> dataServiceList = dataServiceCodeRepositroy.findAll( pageable);
        return dataServiceList;
    }

    public DataServiceCode save(String userId , AddDataServiceCodeDTO add) throws IOException {
        JSONObject jsonObject = createXmlJson(add.getDependencyItemList(),add.getInputItemList(), add.getOutputItemList(), add.getParameterItemList());
//        String ip = dataContainerService.getIpByToken(add.getToken());
        String codeFileUrl= uploadCodeFile(add,userId);

        DataServiceCode dataServiceCode = new DataServiceCode();
        add.convertTo(dataServiceCode);
        dataServiceCode.setUserId(userId);
        dataServiceCode.setContentPath(codeFileUrl);
        return dataServiceCodeRepositroy.insert(dataServiceCode);
    }

    public JSONObject createXmlJson(List<DependencyItem> dependencyItemList,List<InputItem> inputItemList,
            List<OutputItem> outputItemList,List<ParameterItem> parameterItemList){
        JSONObject metaDetailJSON = new JSONObject();

        metaDetailJSON.put("Dependency",dependencyItemList);
        metaDetailJSON.put("Input",inputItemList);
        metaDetailJSON.put("Output",outputItemList);
        metaDetailJSON.put("Parameter",parameterItemList);

        return  metaDetailJSON;
    }

    public String uploadCodeFile(AddDataServiceCodeDTO add, String userId) throws IOException {
        File contentFile = getFile(add.getContent(),userId,add.getName(),"py");

        FileSystemResource resource = new FileSystemResource(contentFile);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("serverNode", "china");
        form.add("userId", userId);
        form.add("datafile", resource);
        form.add("name", add.getName());
        form.add("origination", "reproducibility");
        JSONObject resultJSON = dataContainerService.upload(form);
        String id = resultJSON.getJSONObject("data").getStr("id");
        return "http://221.226.60.2:8082/data/"+id;
    }

}
