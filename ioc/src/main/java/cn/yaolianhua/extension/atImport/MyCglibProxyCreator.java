package cn.yaolianhua.extension.atImport;


import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-20 21:56
 **/
public class MyCglibProxyCreator extends ProxyCreator{

    private Class clazz;

    public MyCglibProxyCreator(Class clazz) {
        this.clazz = clazz;
    }

   @Override
    public Object createProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        /*
        NoOp.INSTANCE：这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截。
        process(...)对应的CallbackFilter中定义的索引0，在Callback[]数组中使用的过滤为interceptor,因此执行了方法拦截器进行拦截。
        process3(...)对应CallbackFilter中定义的索引1，在Callback[]数组中使用的过滤为NoOp，因此直接执行了被代理方法。
         */
        enhancer.setCallbackFilter(new MyCallbackFilter());
        enhancer.setCallbacks(new Callback[]{new MyMethodInterceptor(),NoOp.INSTANCE});
        return enhancer.create();
    }

    class MyMethodInterceptor implements MethodInterceptor{
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            StringBuilder params = new StringBuilder();
            if (objects != null){
                for (Object arg : objects) {
                    String typeName = arg.getClass().getSimpleName();
                    params.append(typeName).append(" ").append(arg).append(",");
                }
            }
            String s = "["+params.toString()+"]";
            StringBuilder sb = new StringBuilder();
            sb.append(" ************************cglib Proxy invoke before*************************").append("\n");
            sb.append("target object:       ").append(o).append("\n");
            sb.append("target method class: ").append(method.getDeclaringClass().getName()).append("\n");
            sb.append("target method name : ").append(method.getName()).append("\n");
            sb.append("target method args : ").append(s).append("\n");
            sb.append("proxy method class : ").append(methodProxy.getClass().getName()).append("\n");
            sb.append("proxy method       : ").append(methodProxy.getSignature().getName());
            System.out.println(sb.toString());
            Object invokeSuper = methodProxy.invokeSuper(o, objects);
            System.out.println("target method invoke return value ["+invokeSuper+"]");
            System.out.println("invoke after");
            return invokeSuper;
        }
    }

    class MyCallbackFilter implements CallbackFilter{
        @Override
        public int accept(Method method) {
            if ("process".equals(method.getName())){
                System.out.println("MyCallbackFilter active in method ["+method.getDeclaringClass().getName() +" "+method.getName()+"]");
                return 0;
            }

            return 1;
        }
    }


}
