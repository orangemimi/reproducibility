package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.njnu.reproducibility.utils.Utils.convertMdl;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/15 16:59
 * @modified By：
 * @version: 1.0.0
 */
@Service
public class RemotePortalService {
    public JSONObject getModelInfo(String id){
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://222.192.7.75:80/computableModel/getInfo/" + id; ////Step0:根据MD5获取可用的任务服务器
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.GET,httpEntity, JSONObject.class);//虚拟http请求

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }

        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return convertMdl(result) ;
    }

    public JSONObject getUnitList(int currentPage, int pagesize) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");


        //        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(headers);

//        RestTemplate restTemplate = new RestTemplate();
//        String urlStr = "http://222.192.7.75:80/repository/searchUnit/"; ////Step0:根据MD5获取可用的任务服务器
//        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.POST,httpEntity, JSONObject.class);//虚拟http请求
//
//        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
//            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
//        }
//
//        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");

        RestTemplate restTemplate = new RestTemplate();
        String urlStr ="http://geomodeling.njnu.edu.cn/repository/getUnits/?asc="+1+"&page="+currentPage+"&size="+pagesize;
//        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
//        form.add("page",currentPage);
//        form.add("size",pagesize);
//        form.add("asc",1);

        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<MultiValueMap>(headers);
        ResponseEntity<JSONObject> jsonObjectResponseEntity;
        try {
            jsonObjectResponseEntity = restTemplate.exchange(urlStr, HttpMethod.GET, httpEntity, JSONObject.class);
        } catch(Exception e) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }

        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");

        return result;
    }

    public JSONObject getModelsByPortal(int page, int pageSize, String searchText) {
        String url = "http://geomodeling.njnu.edu.cn/modelItem/list";
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("sortField", "viewCount");
        map.add("asc", false);
        map.add("page", page);
        map.add("pageSize", pageSize);
//        List<String> list = new ArrayList<>();
//        list.add("all");
        map.add("classifications[]", "all");
        map.add("classType", 2);
        map.add("queryField", "Name");
        map.add("searchText", searchText);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(map, headers);

        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
        return responseEntity.getBody();
    }

    public List<JSONObject> getComputableModels(String oid) {
        String url = "http://geomodeling.njnu.edu.cn/modelItem/searchByOid?oid=" + oid;
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONArray computableIds = result.getBody().getJSONObject("data").getJSONObject("relate").getJSONArray("computableModels");
        List<JSONObject> list = new ArrayList<>();
        for(int i = 0; i < computableIds.size(); i++) {
            String str = computableIds.get(i, String.class);
            String url1 = "http://geomodeling.njnu.edu.cn/computableModel/getInfo/" + str;
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url1, HttpMethod.GET, httpEntity, JSONObject.class);
            if(responseEntity.getBody().getJSONObject("data").getStr("md5") != null) {
                JSONObject temp = new JSONObject();
                temp.put("md5", responseEntity.getBody().getJSONObject("data").getStr("md5"));
                temp.put("name", responseEntity.getBody().getJSONObject("data").getStr("name"));
                temp.put("description", responseEntity.getBody().getJSONObject("data").getStr("description"));
                list.add(temp);
            }
        }
        return list;

    }
}
