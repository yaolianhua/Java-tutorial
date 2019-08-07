package cn.yaolianhua.proxy;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-07 15:22
 **/
public class RetryAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("MethodBeforeAdvice invoke");
    }
}
