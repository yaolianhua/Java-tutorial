package cn.yaolianhua.atAspectSupport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 22:16
 **/
@Component
@Aspect
public class AfterFinallyAdviseAspectExample extends JoinPointExample{

    @After("executeInDaoPackage()")
    public void AfterFinally(JoinPoint joinPoint){
        print(joinPoint,"@After(executeInDaoPackage())");
    }
}
