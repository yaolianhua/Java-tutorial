package cn.yaolianhua.dependencies.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-03 11:09
 **/
public class AnnotationQualifierExample {
    @Autowired
    @Qualifier(value = "main")
    private ExampleBeanQualifierInterface exampleBeanQualifierInterface;
}
