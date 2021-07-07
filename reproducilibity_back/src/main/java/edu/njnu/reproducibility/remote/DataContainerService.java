package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * @ClassName dataItemService
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/16  22:06
 * @Version 1.0.0
 */
@Service
public class DataContainerService {
    @Value("${dataContainer}")
    private String dataContainer;

    public JSONObject addDataItem(File file){
        FileSystemResource resource = new FileSystemResource(file);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", resource);
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + dataContainer  + ":8081" + "/dataitem/addByStorage" ; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr,form, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public ResponseEntity<byte[]> download(String id) {
        RestTemplate restTemplate = new RestTemplate();
//        String urlStr = "http://" + dataContainer + ":8082/data?uid=" + id;
        String urlStr = "http://" + dataContainer + ":8082/data/" + id;
        ResponseEntity<byte []> response = restTemplate.exchange(urlStr, HttpMethod.GET,
                null, byte[].class);
        return  response;
    }

    public JSONObject upload(MultiValueMap<String, Object> form) {
        String urlStr = "http://" + dataContainer + ":8082/data";
//        String urlStr = "http://" + dataContainer + ":8082/dataNoneConfig";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
//        String urlResult = result.getJSONObject("data").getString("source_store_id");
        JSONObject urlResult = result.getJSONObject("data");
        return urlResult;
    }
}
