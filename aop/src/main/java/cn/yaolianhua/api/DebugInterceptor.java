package cn.yaolianhua.api;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-07 22:02
 **/
public class DebugInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: invocation=[" + invocation + "]");
        Object arg = invocation.getArguments()[0];
        if (arg instanceof String && ((String)arg).length() < 10)
        {
            Object proceed = invocation.proceed();
            System.out.println("After: invocation");
            return proceed;
        }
        else {
            return "string param length > 10";
        }

    }
}
