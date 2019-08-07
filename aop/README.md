## Spring aop

------------
[Aop的相关概念](#Aop的相关概念)
[Aop代理](#Aop代理)
[Aspect注解的支持](#Aspect注解的支持)
   - [启用Aspect注解支持](#启用Aspect注解支持)
   - [声明切面](#声明切面)
   - [声明切点](#声明切点)
   - [声明通知](#声明通知)

[基于xml的支持](#基于xml的支持)
[如何选择](#如何选择)
   - [Spring AOP还是Full AspectJ](#Spring AOP还是Full AspectJ)
   - [AspectJ注解或XML for Spring AOP](#AspectJ注解或XML for Spring AOP)

[代理机制](#代理机制)
   - [了解Aop代理](#了解Aop代理)

[编程创建AspectJ注解的代理](#编程创建AspectJ注解的代理)

------------

> Aop 在Spring中用于
- 声明式事物
- 自定义切面，用Aop补充oop

## Aop的相关概念

** aspect**：跨越多个类的关注点的模块。Spring Aop中，切面是基于xml配置的常规类或使用`@Aspect`注解的常规类来实现的

** join point**：Spring Aop中，连接点表示要执行的目标方法

** pointcut**：匹配连接点的谓词，我的理解就是连接点的集合

** advise**：通知（位置+增强逻辑）

** target object**：目标对象（被切面通知的对象，也叫做“被通知对象”），由于Spring AOP是使用运行时代理实现的，因此该对象始终是代理对象

** aop proxy**：由AOP框架创建的对象，用于实现切面契约（通知方法执行等），在Spring Framework中，AOP代理是JDK动态代理或CGLIB代理

** weaving**：将切面与其他应用程序类或对象链接以创建通知对象（我理解此处就是一个代理对象）。这可以在编译时（例如，使用AspectJ编译器），加载时或在运行时完成。与其他纯Java AOP框架一样，Spring AOP在运行时执行织入（将增强逻辑应用到目标对象上的过程）

#### Spring Aop通知类型

** Before advice**：在连接点之前运行的通知，但不能阻止执行流继续到连接点(除非抛出异常)

** After returning advice**：在连接点正常完成后运行的通知(例如，如果方法返回时没有抛出异常)

** After throwing advice**：如果方法通过抛出异常退出，则执行通知

** After (finally) advice**：执行通知，不管连接点以何种方式退出(正常或异常返回)

** Around advice**：围绕连接点(如方法调用)的通知。这是最有力的通知。Around通知可以在方法调用前后执行自定义行为。它还负责选择是继续到连接点，还是通过返回它自己的返回值或抛出异常来简化建议的方法执行

## Aop代理

Spring AOP`默认`为AOP代理使用标准`JDK动态代理`。这允许代理任何接口(或一组接口)。

Spring AOP还可以使用CGLIB代理。这对于代理类而不是接口是必要的。`默认情况下，如果业务对象没有实现接口，则使用CGLIB`。由于面向接口编程而非面向类是一种很好的实践，业务类通常实现一个或多个业务接口。强制使用CGLIB是可能的，在那些(希望很少)情况下，您需要通知在接口上没有声明的方法，或者需要将代理对象作为具体类型传递给方法

## Aspect注解的支持

> @AspectJ指的是将切面声明为使用注解注释的常规Java类的风格。作为AspectJ 5版本的一部分，AspectJ项目引入了@AspectJ风格。Spring使用AspectJ提供的库解释与AspectJ 5相同的注释，用于切入点解析和匹配。但是，AOP运行时仍然是纯Spring AOP，并且不依赖于AspectJ编译器或weaver

### 启用Aspect注解支持

要在Spring配置中使用@AspectJ方面，您需要启用Spring支持，以基于@AspectJ方面配置Spring AOP，并根据这些方面是否建议自动代理bean。通过自动代理，如果Spring确定bean被一个或多个切面通知，它会自动为该bean生成一个代理来拦截方法调用，并确保根据需要执行通知。

可以使用XML或Java样式配置启用@AspectJ支持。在任何一种情况下，您还需要确保`AspectJ的aspectjweaver.jar`库位于应用程序的类路径中

#### 使用Java配置启用@AspectJ支持

```java
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "cn.yaolianhua")
public class AppConfig {
}
```

### 声明切面

启用@AspectJ支持后，在应用程序上下文中定义的任何bean都具有@AspectJ方面的类（具有@Aspect注释），Spring会自动检测并用于配置Spring AOP

您可以在Spring XML配置中将方面类注册为常规bean，或者通过类路径扫描自动检测它们 - 与任何其他Spring管理的bean相同。但请注意，@Aspect注释不足以在类路径中进行自动检测。为此，您需要添加单独的@Component注释（或者，根据Spring的组件扫描程序的规则，可以添加符合条件的自定义构造型注释）

```java
@Component
@Aspect
public class AfterFinallyAdviseAspectExample{

}
```

### 声明切点

切入点确定感兴趣的连接点，从而使我们能够控制建议何时执行。Spring AOP仅支持Spring bean的方法执行连接点，因此您可以将切入点视为匹配Spring bean上方法的执行。切入点声明有两个部分：一个包含名称和任何参数的签名，以及一个精确确定我们感兴趣的方法执行的切入点表达式。在AOP的@AspectJ注释样式中，切入点签名由常规方法定义提供，并使用@Pointcut注释指示切入点表达式（用作切入点签名的方法必须具有`void`返回类型）

```java
@Pointcut("execution(public * *(..) )")
    public void anyPublicMethod(){}
```

#### Spring Aop 支持的切点指示符（PCD）

**`execution`**: 用于匹配方法执行连接点。这是使用Spring AOP时要使用的主要切入点PCD

**`within`**:将匹配限制为特定类型中的连接点(使用Spring AOP时在匹配类型中声明的方法的执行)

**`this`**:将匹配限制为连接点(使用Spring AOP时方法的执行)，其中bean引用(Spring AOP代理)是给定类型的实例

**`target`**:将匹配限制为连接点(使用Spring AOP时方法的执行)，其中目标对象(代理的应用程序对象)是给定类型的实例

**`args`**:将匹配限制为连接点(使用Spring AOP时方法的执行)，其中的参数是给定类型的实例

**`@target`**:将匹配限制为连接点(使用Spring AOP时方法的执行)，其中执行对象的类型具有给定类型的注解

**`@args`**:将匹配限制为连接点(使用Spring AOP时方法的执行)，其中传递的实际参数的运行时类型具有给定类型的注解

**`@within`**:限制与具有给定注释的类型中的连接点的匹配(使用Spring AOP时，使用给定注解在类型中声明的方法的执行)。

**`@annotation`**:将匹配限制为连接点的主题(在Spring AOP中执行的方法)具有给定注解的连接点

#### 组合切点表达式

> 可以使用&&, ||和! 组合切入点表达式

```java
@Pointcut("anyPublicMethod() && executeInServiceInterface()")
    public void servicePublic(){}
```

#### 公共切入点

```java
public class JoinPointExample {
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
```

### 声明通知

#### @Before

```java
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
```

#### @AfterReturning

```java
@Component
@Aspect
public class AfterReturningAdviseAspectExample extends JoinPointExample{

    @AfterReturning("annotationAtCustomAnnotation() && selectStart()")
    public void afterReturning(){
        System.out.println("@AfterReturning(atAnnotation() && selectStart())");
    }

    @AfterReturning(value = "annotationAtCustomAnnotation() && selectStart()",returning = "o")
    public void afterReturning(JoinPoint joinPoint,Object o){
        print(joinPoint,"@AfterReturning(value = annotationAtCustomAnnotation() && selectStart(),returning = o) return value ["+o+"]");

    }

}
```

#### @AfterThrowing

```java
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
```

#### @After

```java
@Component
@Aspect
public class AfterFinallyAdviseAspectExample extends JoinPointExample{

    @After("executeInDaoPackage()")
    public void AfterFinally(JoinPoint joinPoint){
        print(joinPoint,"@After(executeInDaoPackage())");
    }
}
```

#### @Around

```java
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
```

## 基于xml的支持

参见[这里](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aop-schema "这里")

## 如何选择

一旦确定某个方面是实现给定需求的最佳方法，您如何决定使用Spring AOP或AspectJ以及Aspect语言（代码）样式，@ AspectJ注释样式还是Spring XML样式？这些决策受到许多因素的影响，包括应用程序要求，开发工具和团队对AOP的熟悉程度

### Spring AOP还是Full AspectJ
使用最简单的方法。Spring AOP比使用完整的AspectJ更简单，因为不需要将AspectJ编译器/ weaver引入开发和构建过程。如果您只需要建议在Spring bean上执行操作，那么Spring AOP是正确的选择。如果需要建议不受Spring容器管理的对象（例如域对象），则需要使用AspectJ。如果您希望建议除简单方法执行之外的连接点（例如，字段获取或设置连接点等），则还需要使用AspectJ

### AspectJ注解或XML for Spring AOP

如果您选择使用Spring AOP，则可以选择@AspectJ或XML样式。需要考虑各种权衡

XML样式可能是现有Spring用户最熟悉的，并且由真正的POJO支持。当使用AOP作为配置企业服务的工具时，XML可能是一个不错的选择（一个好的测试是你是否认为切入点表达式是你可能想要独立改变的配置的一部分）。使用XML样式，从您的配置可以更清楚地了解系统中存在哪些方面

XML风格有两个缺点。首先，它没有完全封装它在一个地方解决的要求的实现。DRY原则规定，系统中的任何知识都应该有单一，明确，权威的表示。使用XML样式时，有关如何实现需求的知识将分支到支持bean类的声明和配置文件中的XML。使用@AspectJ样式时，此信息封装在单个模块中：切面。其次，XML样式在它可以表达的内容方面比@AspectJ样式稍微受限：仅支持“单例”方面实例化模型，并且不可能在XML中声明`组合`命名的切入点

## 代理机制

Spring AOP使用JDK动态代理或CGLIB为给定目标对象创建代理。JDK动态代理内置在JDK中，而CGLIB是一个常见的开源类定义库

如果要代理的目标对象实现至少一个接口，则使用JDK动态代理。目标类型实现的所有接口都是代理的。如果目标对象未实现任何接口，则会创建CGLIB代理

如果要强制使用CGLIB代理（例如，代理为目标对象定义的每个方法，而不仅仅是那些由其接口实现的方法），您可以这样做。但是，您应该考虑以下问题：

- 使用CGLIB时，final无法建议方法，因为它们无法在运行时生成的子类中重写

- 从Spring 4.0开始，代理对象的构造函数不再被调用两次，因为CGLIB代理实例是通过Objenesis创建的。只有当您的JVM不允许构造函数绕过时，您才会看到Spring的AOP支持中的双重调用和相应的调试日志条目

要强制使用CGLIB代理，请将元素`<aop:config>`proxy-target-class属性的值设置为true

```xml
<aop:config proxy-target-class="true">
    <!-- other beans defined here... -->
</aop:config>
```

要在使用@AspectJ自动代理支持时强制CGLIB代理，请将元素`<aop:aspectj-autoproxy>`的proxy-target-class属性设置为true

```xml
<aop:aspectj-autoproxy proxy-target-class="true"/>
```
### 了解Aop代理

Spring AOP是基于代理的。在编写自己的方面或使用Spring Framework提供的任何基于Spring AOP的方面之前，掌握最后一个语句实际意味着什么的语义是非常重要的

首先考虑一下你有一个普通的，无代理的，没有特别关于它的直接对象引用的场景

```java
public interface Pojo {
    void foo();
    void bar();
}
```

```java
public class SimplePojo implements Pojo {
    @Override
    public void foo() {
        System.out.println("SimplePojo foo()");
        this.bar();
    }

    @Override
    public void bar() {
        System.out.println("SimplePojo bar()");
    }
}
```
```java
public class RetryAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("MethodBeforeAdvice invoke");
    }
}
```
```java
public static void main(String[] args) {
       noProxy();
        proxy();
        exposeProxy();
}
```
```java
private static void proxy(){

        ProxyFactory factory = new ProxyFactory(new SimplePojo());
        factory.addInterface(Pojo.class);
        factory.addAdvice(new RetryAdvice());
        Pojo pojoProxy = (Pojo) factory.getProxy();
        // this is a method call on the proxy!
        pojoProxy.foo();
}
```

如果在对象引用上调用方法，则直接在该对象引用上调用该方法，如下图所示

![](https://file.yaolh.cn/data/images/blog/spring/aop-proxy-plain-pojo-call.png)

当客户端代码具有的引用是代理时，事情会稍微改变

![](https://file.yaolh.cn/data/images/blog/spring/aop-proxy-call.png)

这里需要理解的关键是main类的main(..)方法中的客户机代码有对代理的引用。这意味着对该对象引用的方法调用是对代理的调用。因此，代理可以委托给与特定方法调用相关的所有拦截器(通知)。然而，一旦调用最终到达目标对象(在本例中是SimplePojo引用)，它可能对自身执行的任何方法调用，比如this.bar()或this.foo()，都将针对这个引用而不是代理来调用。这具有重要意义。这意味着自调用不会导致与方法调用关联的通知有机会执行

好吧，那我们该怎么做呢?最好的方法(这里使用的术语“最佳”比较松散)是重构代码，这样就不会发生自调用。这确实需要您做一些工作，但这是最好的、侵入性最小的方法。下一个方法绝对是可怕的，我们不愿意指出它，正是因为它是如此可怕。您可以(尽管对我们来说很痛苦)将类中的逻辑完全绑定到Spring AOP，如下面的示例所示

```java
public class SimplePojoProxy implements Pojo {
    @Override
    public void foo() {
        System.out.println("SimplePojoProxy foo()");
        ((Pojo)AopContext.currentProxy()).bar();
    }

    @Override
    public void bar() {
        System.out.println("SimplePojoProxy bar()");
    }
}
```

这完全将您的代码与Spring AOP结合在一起，并使类本身意识到它是在AOP上下文中使用的，这与AOP背道而驰。在创建代理时，还需要一些额外的配置，如下面的示例所示

```java
private static void exposeProxy(){
        ProxyFactory factory = new ProxyFactory(new SimplePojoProxy());
        factory.addInterface(Pojo.class);
        factory.addAdvice(new RetryAdvice());
        factory.setExposeProxy(true);
        Pojo pojoProxy = (Pojo) factory.getProxy();
        // this is a method call on the proxy!
        pojoProxy.foo();
    }
```

最后，必须注意AspectJ没有这个自调用问题，因为它不是一个基于代理的AOP框架

结果清单

```java
SimplePojo foo()
SimplePojo bar()

MethodBeforeAdvice invoke
SimplePojo foo()
SimplePojo bar()

MethodBeforeAdvice invoke
SimplePojoProxy foo()
MethodBeforeAdvice invoke
SimplePojoProxy bar()
```

## 编程创建AspectJ注解的代理

除了通过在`<aop:config> `还是`<aop:aspectj-autoproxy>`配置中声明切面，还可以以编程方式创建通知目标对象的代理

您可以使用`org.springframework.aop.aspectj.annotation.AspectJProxyFactory`该类为一个或多个@AspectJ方面建议的目标对象创建代理

```java
// create a factory that can generate a proxy for the given target object
AspectJProxyFactory factory = new AspectJProxyFactory(targetObject);

// add an aspect, the class must be an @AspectJ aspect
// you can call this as many times as you need with different aspects
factory.addAspect(SecurityManager.class);

// you can also add existing aspect instances, the type of the object supplied must be an @AspectJ aspect
factory.addAspect(usageTracker);

// now get the proxy object...
MyInterfaceType proxy = factory.getProxy();
```
