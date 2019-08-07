package cn.yaolianhua.proxy;

import org.springframework.aop.framework.AopContext;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-07 15:06
 **/
public class SimplePojoProxy implements Pojo {
    @Override
    public void foo() {
        System.out.println("SimplePojoProxy foo()");
        ((Pojo)AopContext.currentProxy()).bar();
    }

    @Override
    public void bar() {
        System.out.println("SimplePojoProxy bar()");
    }
}
