package cn.yaolianhua.extension.beanPostProcessor;

import cn.yaolianhua.extension.atImport.MyCglibProxyCreator;
import cn.yaolianhua.extension.atImport.MyJdkProxyCreator;
import cn.yaolianhua.extension.atImport.ProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-20 16:59
 **/
@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;
    private  Boolean proxy;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (proxy != null && beanName.startsWith("service"))
        {
            ProxyCreator proxyCreator = proxy ? new MyJdkProxyCreator(beanClass) : new MyCglibProxyCreator(beanClass);
            return proxyCreator.createProxy();
        }
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory.containsBean("proxyCreator"))
            proxy = ((ProxyCreator) beanFactory.getBean("proxyCreator")).getProxy();
    }
}
