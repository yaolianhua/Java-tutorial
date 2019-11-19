package cn.yaolianhua.api;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-13 14:07
 **/
public class MyBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method m, Object[] args, Object target) throws Throwable {
        System.out.println("before");
        System.out.println("method ["+m+"]");
        System.out.println("args ["+args.length+"]");
        System.out.println("target ["+target+"]");
    }

}
