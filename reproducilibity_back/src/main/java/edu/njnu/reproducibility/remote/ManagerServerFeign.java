 package edu.njnu.reproducibility.remote;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author Zhiyi
 * @Date 2020/6/2  11:31
 * @Version 1.0.0
 */
@FeignClient(name = "managerServer",
        url = "http://222.192.7.75:8084",
        fallback = ManagerServerFeign.FeignServiceFallBack.class)
@Primary
public interface ManagerServerFeign {

    @RequestMapping(value = "/GeoModeling/taskNode/getServiceTask/{pid}",method = RequestMethod.GET)
    JSONObject getServiceTask(@PathVariable String pid);

    @RequestMapping(value = "/GeoModeling/computableModel/createTask",method = RequestMethod.POST)
    JSONObject createTask(@RequestBody JSONObject createTaskJson);

    @RequestMapping(value = "/GeoModeling/computableModel/invoke",method = RequestMethod.POST)
    JSONObject invoke(@RequestBody JSONObject invokeJson);

    @RequestMapping(value = "/GeoModeling/computableModel/refreshTaskRecord",method = RequestMethod.POST)
    JSONObject refresh(@RequestBody JSONObject refreshJson);

    @Component
    class FeignServiceFallBack implements ManagerServerFeign {

        @Override
        public JSONObject getServiceTask(String pid) {
            return null;
        }

        @Override
        public JSONObject createTask(JSONObject createTaskJson) {
            return null;
        }

        @Override
        public JSONObject invoke(JSONObject invokeJson) {
            return null;
        }

        @Override
        public JSONObject refresh(JSONObject refreshJson) {
            return null;
        }

    }
}
