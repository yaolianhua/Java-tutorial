package cn.yaolianhua.dependencies.annotation;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-02 17:52
 **/
public class AnnotationAutowiredExample {
    @Autowired
    private ExampleBean exampleBean;
    private ExampleBean2 exampleBean2;
    private ExampleBean3 exampleBean3;

    @Autowired
    public AnnotationAutowiredExample(ExampleBean2 exampleBean2) {
        this.exampleBean2 = exampleBean2;
    }
    @Autowired
    public void setExampleBean3(ExampleBean3 exampleBean3) {
        this.exampleBean3 = exampleBean3;
    }

    public ExampleBean getExampleBean() {
        return exampleBean;
    }

    public ExampleBean2 getExampleBean2() {
        return exampleBean2;
    }

    public ExampleBean3 getExampleBean3() {
        return exampleBean3;
    }
}
