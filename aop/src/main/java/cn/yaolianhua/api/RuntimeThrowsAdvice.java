package cn.yaolianhua.api;

import org.springframework.aop.ThrowsAdvice;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-13 14:28
 **/
public class RuntimeThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(RuntimeException e) throws Throwable{
        System.out.println(" error ["+e+"]");
    }
}
