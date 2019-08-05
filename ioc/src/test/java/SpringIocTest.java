
import cn.yaolianhua.dependencies.annotation.AnnotationAutowiredExample;
import cn.yaolianhua.dependencies.annotation.ExampleBeanPrimaryInterface;
import cn.yaolianhua.dependencies.annotation.ExampleBeanQualifierInterface;
import cn.yaolianhua.dependencies.annotation.ExampleBeanResourceInterface;
import cn.yaolianhua.dependencies.configuration_detail.collection.CollectionExample;
import cn.yaolianhua.dependencies.configuration_detail.collection.CollectionMergingExample;
import cn.yaolianhua.dependencies.configuration_detail.depends_on.DependsOnExample;
import cn.yaolianhua.dependencies.configuration_detail.mi.MethodInjectionExample2;
import cn.yaolianhua.dependencies.configuration_detail.namespace.Cnamespace;
import cn.yaolianhua.dependencies.configuration_detail.straight_values.DataSourceExample;
import cn.yaolianhua.dependencies.di.circular.CircularBean1;
import cn.yaolianhua.dependencies.di.circular.CircularBean2;
import cn.yaolianhua.dependencies.di.constructor.ExampleBean;
import cn.yaolianhua.dependencies.di.constructor.SimpleBean;
import cn.yaolianhua.dependencies.di.constructor.SimpleService;
import cn.yaolianhua.dependencies.di.example.ExampleBean3;
import cn.yaolianhua.dependencies.di.example.SimpleBean3;
import cn.yaolianhua.dependencies.di.setter.ExampleBean2;
import cn.yaolianhua.dependencies.di.setter.SimpleBean2;
import cn.yaolianhua.dependencies.extensionPoints.MyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-07-31 16:29
 **/
public class SpringIocTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("** constructorDI **");
        constructorDI();
        System.out.println("** setterDI **");
        setterDI();
        System.out.println("** factoryMethodDI **");
        factoryMethodDI();
        System.out.println("** circularDI **");
        circularDI();
        System.out.println("** datasourceStraightValues **");
        datasourceStraightValues();
        System.out.println("** collectionDetail **");
        collectionDetail();
        System.out.println("**  collectionMerging  **");
        collectionMerging();
        System.out.println("** pNamespace **");
        pNamespace();
        System.out.println("** cNamespace **");
        cNamespace();
        System.out.println("** depend-on **");
        dependsOn();
        System.out.println("** lazyInit **");
        lazyInit();
        System.out.println("** methodInjection **");
        methodInjection();
        System.out.println("** lifecycleCallbacks **");
        lifecycleCallbacks();
        System.out.println("** beanPostProcessor **");
        beanPostProcessor();
        System.out.println("** beanFactoryPostProcessor **");
        beanFactoryPostProcessor();
        System.out.println("** annotation **");
        annotation();
    }
    private static void annotation(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:annotation/annotation.xml");
        System.out.println(context.getBean(AnnotationAutowiredExample.class).getExampleBean3().getStr());
        System.out.println(context.getBean(AnnotationAutowiredExample.class).getExampleBean2().getString());
        System.out.println(context.getBean(AnnotationAutowiredExample.class).getExampleBean().getString());
//        System.out.println(context.getBean(AnnotationRequiredExample.class).getExampleBean().getString());//NullPointerException
        System.out.println(context.getBean(ExampleBeanPrimaryInterface.class).getString());
        System.out.println(context.getBean(ExampleBeanQualifierInterface.class).getString());//expected single matching bean but found 2: main,action
        System.out.println(context.getBean(ExampleBeanResourceInterface.class).getString());
    }

    private static void beanFactoryPostProcessor(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:extensionPoints/extension-points.xml");
        System.out.println(context.getBean("exampleBean"));
        System.out.println(context.getBean("exampleBean"));
    }

    private static void beanPostProcessor(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:extensionPoints/extension-points.xml");
        MyService myService = (MyService) context.getBean("exampleBean");
        System.out.println("myService instanceof Proxy ? "+(myService instanceof Proxy));
        myService.service();
    }

    private static void lifecycleCallbacks() throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:customBean/custom-bean.xml");

        TimeUnit.SECONDS.sleep(1);
        context.close();
    }

    private static void methodInjection(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/method-injection.xml");
        context.refresh();
//        System.out.println(context.getBean(MethodInjectionExample.class).getScopeExampleBean().hashCode());
//        System.out.println(context.getBean(MethodInjectionExample.class).getScopeExampleBean().hashCode());
        System.out.println(context.getBean(MethodInjectionExample2.class).getScopeExampleBean().hashCode());
        System.out.println(context.getBean(MethodInjectionExample2.class).getScopeExampleBean().hashCode());
    }


    private static void lazyInit(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/depends-on.xml");
        context.refresh();

    }

    private static void dependsOn(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/depends-on.xml");
        context.refresh();
        System.out.println(context.getBean(DependsOnExample.class).hashCode());
    }

    private static void cNamespace(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/namespace.xml");
        context.refresh();
        System.out.println(context.getBean(Cnamespace.class));
    }

    private static void pNamespace(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/namespace.xml");
        context.refresh();
        System.out.println(context.getBean("john-classic"));
        System.out.println(context.getBean("john-modern"));
        System.out.println(context.getBean("jane"));
    }

    private static void collectionMerging(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:configuration_detail/collection-merging.xml");
        context.refresh();
        System.out.println(context.getBean(CollectionMergingExample.class).getProperties());
    }

    private static void collectionDetail(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:configuration_detail/collection.xml");
        System.out.println(context.getBean(CollectionExample.class).getList());
        System.out.println(context.getBean(CollectionExample.class).getMap());
        System.out.println(context.getBean(CollectionExample.class).getSet());
        System.out.println(context.getBean(CollectionExample.class).getProperties());
    }

    private static void datasourceStraightValues(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:configuration_detail/straight-values.xml");
        System.out.println(context.getBean(DataSourceExample.class).getDataSource());
    }

    private static void constructorDI(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:di/constructor.xml");
        context.refresh();
        System.out.println(context.getBean(SimpleService.class).hashCode());
        System.out.println(context.getBean(SimpleBean.class).hashCode());
        System.out.println(context.getBean(ExampleBean.class).hashCode());
    }

    private static void setterDI(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:di/setter.xml");
        context.refresh();
        System.out.println(context.getBean(ExampleBean2.class).hashCode());
        System.out.println(context.getBean(SimpleBean2.class).hashCode());
    }

    private static void factoryMethodDI(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:di/static-method.xml");
        System.out.println(context.getBean(SimpleBean3.class).hashCode());
        System.out.println(context.getBean(ExampleBean3.class).hashCode());
    }

    private static void circularDI(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:di/circular.xml");
//        System.out.println(context.getBean(ExampleBean5.class).hashCode());
//        System.out.println(context.getBean(ExampleBean4.class).hashCode());//BeanCurrentlyInCreationException

        System.out.println(context.getBean(CircularBean1.class).hashCode());
        System.out.println(context.getBean(CircularBean2.class).hashCode());
    }
}
