package cn.yaolianhua.atAspectSupport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 17:36
 **/
public class JoinPointExample {
    protected void print(JoinPoint joinPoint,String advise){
        Object[] args = joinPoint.getArgs();
        StringBuilder param = new StringBuilder();
        for (Object arg:args){
            param.append(arg.getClass().getTypeName()).append(" ").append(arg).append(",");
        }
        param = new StringBuilder(param.substring(0, param.length() - 1));
        StringBuilder sb = new StringBuilder();
        sb.append(" ************* ").append(advise).append(" start ************* ").append("\n");
        sb.append(" method signature: ").append(joinPoint.getSignature()).append("\n");
        sb.append(" method param    : ").append("[").append(param).append("]").append("\n");
        sb.append(" method kind     : ").append(joinPoint.getKind()).append("\n");
        sb.append(" method target   : ").append(joinPoint.getTarget().getClass().toString()).append("\n");
        sb.append(" method this     : ").append(joinPoint.getThis().getClass().toString()).append("\n");
        sb.append(" ************* ").append(advise).append(" end ************* ").append("\n");
        System.out.println(sb);
        System.out.println();

    }
    //执行任何公共方法
    @Pointcut("execution(public * *(..) )")
    public void anyPublicMethod(){}

    @Pointcut("anyPublicMethod() && executeInServiceInterface()")
    public void servicePublic(){}

    //执行名称以select开头的任何方法
    @Pointcut("execution(* select*(..) )")
    public void selectStart(){}

    //执行ServiceInterface接口定义的任何方法
    @Pointcut("execution(* cn.yaolianhua.service.ServiceInterface.*(..))")
    public void executeInServiceInterface(){}

    //执行dao包中定义的任何方法
    @Pointcut("execution(* cn.yaolianhua.dao.*.*(..))")
    public void executeInDaoPackage(){}

    //执行dao包或其子包中定义的任何方法
    @Pointcut("execution(* cn.yaolianhua.dao..*.*(..))")
    public void executeInDaoPackageAndSubPackage(){}

    //service包中的任何连接点（仅在Spring AOP中执行方法）
    @Pointcut("within(cn.yaolianhua.service.*)")
    public void executeJoinpointInServicePackage(){}

    //service包或其子包中的任何连接点（仅在Spring AOP中执行方法）
    @Pointcut("within(cn.yaolianhua.service..*)")
    public void executeJoinpointInServicePackageAndSubPackage(){}

    //实现DaoInterface接口的任何连接点的代理对象（仅在Spring AOP中执行方法）
    @Pointcut("this(cn.yaolianhua.dao.DaoInterface)")
    public void thisDaoInterface(){}

    //实现DaoInterface接口的任何连接点的目标对象（仅在Spring AOP中执行方法）
    @Pointcut("target(cn.yaolianhua.dao.DaoInterface)")
    public void targetDaoInterface(){}

    //采用单个参数(在运行时传递的参数)的任何连接点（仅在Spring AOP中执行的方法）
    @Pointcut("args(java.lang.String)")
    public void argsFirstString(){}

    //目标对象具有@CustomAnnotation注释的任何连接点（仅在Spring AOP中执行方法）
    @Pointcut("@target(cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation)")
    public void targetWithAtCustomAnnotation(){}

    //任何连接点（仅在Spring AOP中执行方法），其中目标对象的声明类型具有@CustomAnnotation注释
    @Pointcut("@within(cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation)")
    public void withinAtCustomAnnotation(){}

    //任何连接点（仅在Spring AOP中执行方法），其中执行方法具有 @CustomAnnotation注释
    @Pointcut("@annotation(cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation)")
    public void annotationAtCustomAnnotation(){}

    //任何连接点（仅在Spring AOP中执行的方法），它接受一个参数，并且传递的参数的运行时类型具有@CustomAnnotation注释
    @Pointcut("@args(cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation)")
    public void argsWithAtCustomAnnotation(){}

    //具有与*Service通配符表达式匹配的名称的Spring bean上的任何连接点（仅在Spring AOP中执行方法）
    @Pointcut("bean(*Service)")
    public void beanName(){}
}
