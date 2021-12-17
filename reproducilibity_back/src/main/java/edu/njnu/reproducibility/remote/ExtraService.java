package edu.njnu.reproducibility.remote;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/12/14/20:10
 * @Description:
 */

@Service
public class ExtraService {

    /**
    * @Description:百度地图API获取行政区域
    * @Author: Yiming
    * @Date: 2021/12/14
    */
    public JSONObject getCityList() {
        String url = "http://api.map.baidu.com/api_region_search/v1/?keyword=中国&sub_admin=3&ak=duBHPLUvDk5T3GgLF2o66hqSG3hnGTw5";
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        return result.getBody();
    }
}
