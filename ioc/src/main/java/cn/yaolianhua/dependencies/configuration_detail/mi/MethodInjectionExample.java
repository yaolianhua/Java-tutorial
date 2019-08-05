package cn.yaolianhua.dependencies.configuration_detail.mi;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 16:49
 **/
public class MethodInjectionExample implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ScopeExampleBean getScopeExampleBean(){
        return this.applicationContext.getBean(ScopeExampleBean.class);
    }
}
