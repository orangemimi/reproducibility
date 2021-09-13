package edu.njnu.reproducibility.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ZhangYiming
 * @Date: 2021/08/17/16:07
 * @Description: 该注解可以用在@Controller层中的方法,指明该方法的调用需要项目身份验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedProAuth {
}
