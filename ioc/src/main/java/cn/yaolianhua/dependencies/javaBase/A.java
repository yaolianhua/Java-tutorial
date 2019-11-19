package cn.yaolianhua.dependencies.javaBase;

import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 10:46
 **/
@Component
@Flag(value = true)// false -> NoSuchBeanDefinitionException
public class A {
    public void doSomething(){
        System.out.println("A doSomething()");
    }
}
