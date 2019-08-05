package cn.yaolianhua.dependencies.customBean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 10:29
 **/
public class LifecycleCallbacksExample implements InitializingBean , DisposableBean {
    private String string;
    public LifecycleCallbacksExample() {
        System.out.println("LifecycleCallbacksExample Instantiation");
    }

    public void setString(String string) {
        this.string = string;
        System.out.println("LifecycleCallbacksExample properties set["+string+"]");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet invoked");
    }

    public void destroy() throws Exception {
        System.out.println("destroy invoked");
    }
}
