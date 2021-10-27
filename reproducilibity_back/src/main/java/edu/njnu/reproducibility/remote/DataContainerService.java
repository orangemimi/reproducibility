package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getIpByToken(String token) throws UnsupportedEncodingException {
        String urlStr = "http://111.229.14.128:8898/state?token="+ URLEncoder.encode(token);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type","application/json");
//        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
//        String urlResult = result.getJSONObject("data").getString("source_store_id");
        JSONObject urlResult = result.getJSONObject("ip");
        return "urlResult";
    }

    public Object getDataService(String id, String token, String type) {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://111.229.14.128:8898/capability?id=" + id + "&type="+type+"&token="+ token;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, headers);
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr,HttpMethod.GET, requestEntity, JSONObject.class);

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }

        JSONObject  result = jsonObjectResponseEntity.getBody().getJSONObject("capability");//获得上传数据的URL
        return result;
    }

    public String findData(String token, String name) {
        String url = "http://111.229.14.128:8898/findData";
        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        param.put("modelName", "instances");
        JSONObject searchCont = new JSONObject();
        searchCont.put("type", "Processing");
        searchCont.put("name", name);
        param.put("searchCont", searchCont);
        HttpHeaders headers = new HttpHeaders();

        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.add("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity(param, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            //设置RestTemplate编码格式，restemplate底层是默认使用ISO-8859-1编码
            restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            String body = result.getBody();
//            body = new String(body.getBytes("ISO8859-1"), "UTF-8");
            return body;
        }catch (Exception e) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
    }

    public String findAllProcessing(String token) {
        String url = "http://111.229.14.128:8898/findData";
        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        param.put("modelName", "instances");
        JSONObject searchCont = new JSONObject();
        searchCont.put("type", "Processing");
        param.put("searchCont", searchCont);
        HttpHeaders headers = new HttpHeaders();

        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.add("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity(param, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            String body = result.getBody();

            return body;
        }catch (Exception e) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
    }
}
