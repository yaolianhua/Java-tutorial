package cn.yaolianhua.extension.beanFactoryPostProcessor;

import cn.yaolianhua.extension.atImport.ProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-19 09:57
 **/
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        boolean containsBeanDefinition = beanFactory.containsBeanDefinition("proxyCreator");
        if (containsBeanDefinition){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("proxyCreator");
            GenericBeanDefinition proxyCreator = (GenericBeanDefinition) beanDefinition;
            Boolean proxy = (Boolean) proxyCreator.getAttribute("proxy");
            ProxyCreator bean = (ProxyCreator) beanFactory.getBean("proxyCreator");
            bean.setProxy(proxy);
        }
    }
}
