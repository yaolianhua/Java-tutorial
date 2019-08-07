package cn.yaolianhua.atAspectSupport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:31
 **/
@Component
@Aspect
public class AfterReturningAdviseAspectExample extends JoinPointExample{

//    @AfterReturning("annotationAtCustomAnnotation() && selectStart()")
//    public void afterReturning(){
//        System.out.println("@AfterReturning(atAnnotation() && selectStart())");
//    }

    @AfterReturning(value = "annotationAtCustomAnnotation() && selectStart()",returning = "o")
    public void afterReturning(JoinPoint joinPoint,Object o){
        print(joinPoint,"@AfterReturning(value = annotationAtCustomAnnotation() && selectStart(),returning = o) return value ["+o+"]");

    }

}
