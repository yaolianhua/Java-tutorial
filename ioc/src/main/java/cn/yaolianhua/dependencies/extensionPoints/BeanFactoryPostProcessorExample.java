package cn.yaolianhua.dependencies.extensionPoints;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 16:18
 **/
public class BeanFactoryPostProcessorExample implements BeanFactoryPostProcessor, Ordered {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("exampleBean");
        beanDefinition.setScope("prototype");
    }

    public int getOrder() {
        return 0;
    }
}
