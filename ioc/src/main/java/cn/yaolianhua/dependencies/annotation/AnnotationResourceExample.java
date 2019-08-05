package cn.yaolianhua.dependencies.annotation;

import javax.annotation.Resource;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-03 11:09
 **/
public class AnnotationResourceExample {
    @Resource(name = "example5")
    private ExampleBeanResourceInterface exampleBeanResourceInterface;
    
}
