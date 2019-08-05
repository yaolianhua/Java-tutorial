package cn.yaolianhua.dependencies.customBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 10:29
 **/
public class LifecycleCallbacksExampleWithAnnotation{
    private String string;
    public LifecycleCallbacksExampleWithAnnotation() {
        System.out.println("LifecycleCallbacksExampleWithAnnotation Instantiation");
    }

    public void setString(String string) {
        this.string = string;
        System.out.println("LifecycleCallbacksExampleWithAnnotation properties set["+string+"]");
    }
    @PostConstruct
    public void myInit(){
        System.out.println("myInit invoked ");
    }

    @PreDestroy
    public void myDestroy(){
        System.out.println("myDestroy invoked");
    }
}
