package cn.yaolianhua.extension.beanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-16 15:33
 **/
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    public MyBeanDefinitionRegistryPostProcessor() {
//        System.out.println(this.getClass().getSimpleName() + " init");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        System.out.println(getClass().getSimpleName() + " postProcessBeanDefinitionRegistry()" );
//        for (String definitionName : registry.getBeanDefinitionNames()) {
//            System.out.println(definitionName);
//        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        System.out.println(getClass().getSimpleName() + " postProcessBeanFactory()");
    }
}
