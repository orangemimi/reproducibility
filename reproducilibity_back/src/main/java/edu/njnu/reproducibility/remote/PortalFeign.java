package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/29 10:27
 * @modified By：
 * @version: 1.0.0
 */
@FeignClient(name = "portal",
        url = "http://222.192.7.75:80",
        fallback = PortalFeign.FeignServiceFallBack.class)
@Primary
public interface PortalFeign {
    @RequestMapping(value ="/computableModel/getInfo/{doi}",method = RequestMethod.GET)
    JSONObject getComputeModel(@PathVariable(value="doi") String doi);

    @Component
    class FeignServiceFallBack implements PortalFeign {
        @Override
        public JSONObject getComputeModel(String doi) {
            return null;
        }
    }
}
