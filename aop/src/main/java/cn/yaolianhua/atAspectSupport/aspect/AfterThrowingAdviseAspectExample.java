package cn.yaolianhua.atAspectSupport.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 21:40
 **/
@Aspect
@Component
public class AfterThrowingAdviseAspectExample{

    @AfterThrowing("execution(* cn.yaolianhua.service.ServiceExample.update(..))")
    public void afterThrowing(){
        System.out.println("@AfterThrowing(execution(* cn.yaolianhua.service.ServiceExample.update()))");
    }

    @AfterThrowing(value = "execution(* cn.yaolianhua.service.ServiceExample.update(..))",throwing = "ex")
    public void afterThrowingEx(RuntimeException ex){
        System.out.println("@AfterThrowing(execution(* cn.yaolianhua.service.ServiceExample.update())) exception:["+ex.getMessage()+"]");
    }
}
