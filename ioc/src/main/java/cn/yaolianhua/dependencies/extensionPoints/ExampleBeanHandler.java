package cn.yaolianhua.dependencies.extensionPoints;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 12:13
 **/
public class ExampleBeanHandler implements InvocationHandler {

    private Object target;
    public ExampleBeanHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("service")){

            System.out.println("... before ...");
            Object invoke = method.invoke(target, args);
            System.out.println("... after ...");
            return invoke;
        }

        return method.invoke(target,args);
    }
}
