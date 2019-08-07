package cn.yaolianhua.atAspectSupport.aspect;

import cn.yaolianhua.pojo.Person;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 22:16
 **/
@Component
@Aspect
public class AroundAdviseAspectExample extends JoinPointExample{

    @Around("executeInDaoPackage()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("@Around(executeInDaoPackage()) invoke proceed() before");
        System.out.println();
        Object o = pjp.proceed();
        System.out.println("@Around(executeInDaoPackage()) invoke proceed() after");
        System.out.println();
        print(pjp,"@Around(executeInDaoPackage())");
        Person person = (Person) o;
        System.out.println("the old return value ["+person+"]");
        Person newPerson = new Person(6,person.getName(),person.getAge());
        System.out.println("the new return value ["+newPerson+"]");
        return newPerson;
    }
}
