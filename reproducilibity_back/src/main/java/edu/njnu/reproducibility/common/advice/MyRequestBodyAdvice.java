package edu.njnu.reproducibility.common.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @ClassName MyRequestBodyAdvice
 * @Description 针对所有以@RequestBody的请求
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@ControllerAdvice
public class MyRequestBodyAdvice implements RequestBodyAdvice {

    // 在调用本接口三个对方法体和参数进行处理的方法前都需要调用supports方法来
    // 判断是否支持该方法
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }


    // 如果方法体不为空，则在读取方法体前先执行该方法
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }


    // 在已经读取了方法体的内容后调用的方法，可以直接对方法体进行操作
    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    // 如果方法体为空所进行的处理
    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
