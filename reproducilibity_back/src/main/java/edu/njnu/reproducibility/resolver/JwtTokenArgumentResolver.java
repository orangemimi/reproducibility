package edu.njnu.reproducibility.resolver;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JwtUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author     ：Zhiyi
 * @Date       ：2020/12/11 23:25
 * @modified By：
 * @version:     1.0.0
 */

public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JwtTokenParser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = JwtUtils.getTokenFromRequest(request);
        JwtTokenParser jwtTokenParser = methodParameter.getParameterAnnotation(JwtTokenParser.class);
        return authorization == null ? null : JwtUtils.parseJWT(authorization,jwtTokenParser.key() );
    }
}
