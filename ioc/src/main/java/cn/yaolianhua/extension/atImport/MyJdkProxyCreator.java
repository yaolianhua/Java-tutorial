package cn.yaolianhua.extension.atImport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-20 09:52
 **/
public class MyJdkProxyCreator extends ProxyCreator {

    private Class clazz;

    public MyJdkProxyCreator(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object createProxy() {
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),new Handler(clazz));
    }


    class Handler implements InvocationHandler{

        private Class clazz;

        public Handler(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            StringBuilder params = new StringBuilder();
            if (args != null){
                for (Object arg : args) {
                    String typeName = arg.getClass().getSimpleName();
                    params.append(typeName).append(" ").append(arg).append(",");
                }
            }
            String s = "["+params.toString()+"]";

            StringBuilder sb = new StringBuilder();
            sb.append(" ***********************JDK Proxy invoke before********************** ").append("\n");
            sb.append("target class:         ").append(method.getDeclaringClass().getName()).append("\n");
            sb.append("target method:        ").append(method.getName()).append("\n");
            sb.append("target method args:   ").append(s);
            System.out.println(sb.toString());
            Object invoke = method.invoke(clazz.newInstance(), args);
            System.out.println("target method invoke return value ["+invoke+"]");
            System.out.println("invoke after");
            return invoke;
        }
    }

}


