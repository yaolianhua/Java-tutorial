package cn.yaolianhua.dependencies.extensionPoints;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.lang.reflect.Proxy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 12:03
 **/
public class BeanPostProcessorExample implements BeanPostProcessor, Ordered {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("exampleBean")){
            System.out.println("BeanPostProcessorExample postProcessBeforeInitialization ");
            return bean;
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("exampleBean")){
            Object proxyInstance = Proxy.newProxyInstance(BeanPostProcessorExample.class.getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new ExampleBeanHandler(bean));
            System.out.println("BeanPostProcessorExample postProcessAfterInitialization ["+proxyInstance.getClass()+"]");

            return proxyInstance;
        }
        return bean;
    }

    public int getOrder() {
        return 0;
    }
}
