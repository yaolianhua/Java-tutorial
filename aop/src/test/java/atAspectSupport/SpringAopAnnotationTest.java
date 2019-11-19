package atAspectSupport;

import cn.yaolianhua.atAspectSupport.AppConfig;
import cn.yaolianhua.dao.DaoInterface;
import cn.yaolianhua.pojo.Person;
import cn.yaolianhua.proxy.Pojo;
import cn.yaolianhua.proxy.RetryAdvice;
import cn.yaolianhua.proxy.SimplePojo;
import cn.yaolianhua.proxy.SimplePojoProxy;
import cn.yaolianhua.service.ServiceInterface;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:23
 **/
public class SpringAopAnnotationTest {

    public static void main(String[] args) {
        aspect();
//        noProxy();
//        proxy();
//        exposeProxy();
    }

    private static void diTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(context.getBean(ServiceInterface.class).find(1));
    }

    private static void aspect(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        DaoInterface daoInterface = ac.getBean(DaoInterface.class);
        daoInterface.select(1);
        System.out.println(" ----------------------------------------------- ");
//        ac.getBean(ServiceInterface.class).update(5,new Person(0,"none",29));
    }

    private static void noProxy(){
        Pojo pojo = new SimplePojo();
        pojo.foo();
    }
    private static void proxy(){
        ProxyFactory factory = new ProxyFactory(new SimplePojo());
        factory.addInterface(Pojo.class);
        factory.addAdvice(new RetryAdvice());
        Pojo pojoProxy = (Pojo) factory.getProxy();
        // this is a method call on the proxy!
        pojoProxy.foo();
    }
    private static void exposeProxy(){
        ProxyFactory factory = new ProxyFactory(new SimplePojoProxy());
        factory.addInterface(Pojo.class);
        factory.addAdvice(new RetryAdvice());
        factory.setExposeProxy(true);
        Pojo pojoProxy = (Pojo) factory.getProxy();
        // this is a method call on the proxy!
        pojoProxy.foo();
    }

}
