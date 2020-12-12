package edu.njnu.reproducibility.config;


import edu.njnu.reproducibility.resolver.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName SpringMVCConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Configuration
public class DemoSpringMvcConfig implements WebMvcConfigurer {

    /**
     * @param resolvers
     *
     * @Description 添加参数resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtTokenArgumentResolver());
    }


}
