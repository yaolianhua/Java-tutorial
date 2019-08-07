package cn.yaolianhua.atAspectSupport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:31
 **/
@Component
@Aspect
public class BeforeAdviseAspectExample extends JoinPointExample{

    @Before("executeInDaoPackage()")
    public void before(JoinPoint joinPoint){
        print(joinPoint,"@Before(executeInDaoPackage())");
    }

    @Before("executeInDaoPackageAndSubPackage() && args(name,..)")
    public void before(JoinPoint joinPoint,String name){
        print(joinPoint,"@Before(executeInDaoPackageAndSubPackage() && args(name,..))");
    }

    @Before(value = "executeJoinpointInServicePackage() && this(beanThis) && target(beanTarget) && args(int)",
            argNames = "jp,beanThis,beanTarget")
    public void before(JoinPoint jp,Object beanThis, Object beanTarget){
        print(jp,"@Before(value = executeJoinpointInServicePackage() && this(beanThis) && target(beanTarget) && args(int ,String)) [beanThis: "+
                beanThis.getClass().toString()+",beanTarget: "+beanTarget.getClass().toString()+"]");

    }

    @Before("annotationAtCustomAnnotation()")
    public void atAnnotationBefore(JoinPoint joinPoint){
        print(joinPoint,"@Before(annotationAtCustomAnnotation())");
    }


}
