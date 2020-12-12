package edu.njnu.reproducibility.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sun_liber
 * @AnnotationName JwtTokenParser
 * @Description 该注解可以用在@Controller层中方法的参数中定义，可通过key获取"id","name","password"
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTokenParser {
    String key() default "userId";
}
