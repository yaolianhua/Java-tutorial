package cn.yaolianhua.dependencies.javaBase;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 15:14
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Conditional(CustomCondition.class)
public @interface Flag {
    boolean value() default true;
}
