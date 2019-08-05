package cn.yaolianhua.dependencies.annotation;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 17:46
 **/
public class AnnotationRequiredExample {
    private ExampleBean exampleBean;

//    @Required
//    public void setExampleBean(ExampleBean exampleBean) {
//        this.exampleBean = exampleBean;
//    }//NullPointerException

    public ExampleBean getExampleBean() {
        return exampleBean;
    }
}
