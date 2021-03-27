package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/28 22:43
 * @modified By：
 * @version: 1.0.0
 */

@FeignClient(name = "dataContainer",
        url = "http://221.226.60.2:8082",
        fallback = DataContainerFeign.FeignServiceFallBack.class)
@Primary
public interface DataContainerFeign {
//    @RequestMapping(value = ":8081/dataitem/addByStorage",method = RequestMethod.POST)
//    JSONObject addData(@RequestBody MultiValueMap<String, Object> form);

    @RequestMapping(value = "data/{id}",method = RequestMethod.GET)
    JSONObject download(@RequestBody String urlStr);


    @RequestMapping(value = "/data",method = RequestMethod.POST)
    JSONObject upload(@RequestBody MultiValueMap<String, Object> form);



    @Component
    class FeignServiceFallBack implements DataContainerFeign {


        @Override
        public JSONObject download(String urlStr) {
            return null;
        }

        @Override
        public JSONObject upload(MultiValueMap<String, Object> form) {
            return null;
        }
    }
}
