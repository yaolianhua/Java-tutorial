## IoC 容器

------------
[Spring IoC容器和Bean简介](#Spring-IoC容器和Bean简介)

[配置元数据的三种方式](#配置元数据的三种方式)

[Dependencies](#Dependencies)
   -  [DI（Dependency Injection）](#DI-（Dependency-Injection）)
   -  [Dependencies and Configuration in Detail](#Dependencies-and-Configuration-in-Detail)

[Bean Scopes](#Bean-Scopes)
   - [singleton scope](#singleton-scope)
   - [prototype scope](#prototype-scope)
   - [单例bean中依赖原型bean](#单例bean中依赖原型bean)
   - [其他范围](#其他范围)

[自定义bean](#自定义bean)
   - [生命周期回调](#生命周期回调)
   - [ApplicationContextAware and BeanNameAware](#ApplicationContextAware-and-BeanNameAware)
   - [Other Aware Interfaces](#Other-Aware-Interfaces)

[IoC 扩展点](#IoC-扩展点)
   - [BeanPostProcessor](#BeanPostProcessor)
   - [BeanFactoryPostProcessor](#BeanFactoryPostProcessor)

[基于注释的配置](#基于注释的配置)
   - [Required](#Required)
   - [Autowired](#Autowired)
   - [Primary](#Primary)
   - [Qualifier](#Qualifier)
   - [Resource](#Resource)

[类路径扫描和托管组件](#类路径扫描和托管组件)

[基于Java的配置](#基于Java的配置)
   - [基本概念：Bean和Configuration](#基本概念：Bean和Configuration)
   - [使用Bean](#使用Bean)
   - [使用Configuration](#使用Configuration)
   - [编写基于Java的配置](#编写基于Java的配置)
   - [有条件的使用Conditional](#有条件的使用Conditional)


------------
## Spring IoC容器和Bean简介

Spring IoC (Inversion of Control ) 也叫做依赖注入（dependency injection）。它是一个过程，在这个过程当中，对象只能通过`构造函数参数`，`工厂方法的参数`或在
从`工厂方法构造或返回的对象实例上`设置属性来定义它们的依赖关系，然后容器在创建bean时注入这些依赖项

在Spring中，构成应用程序主干并由Spring IoC容器管理的对象称为bean。bean是一个由Spring IoC容器实例化，组装和管理的对象。否则，bean只是应用程序中许多对象之一。Bean及其之间的依赖关系反映在容器使用的配置元数据中


[![](https://file.yaolh.cn/data/images/blog/spring/container-magic.png)](https://file.yaolh.cn/data/images/blog/spring/container-magic.png)

## 配置元数据的三种方式

- xml
- Java annotations
- Java config

## Dependencies

### DI（Dependency Injection）

> 依赖注入（DI）是一个过程，通过这个过程，对象只能通过构造函数参数，工厂方法的参数或在构造对象实例后在对象实例上设置的属性来定义它们的依赖关系（即，它们使用的其他对象）。从工厂方法返回。然后容器在创建bean时注入这些依赖项。这个过程基本上是bean本身的反向（因此名称，控制反转），它通过使用类的直接构造或服务定位器模式来控制其依赖项的实例化或位置

DI的两种方式`基于构造函数的依赖注入`和`基于setter的依赖注入`

#### 构造器注入

> 基于构造函数的DI由容器调用具有多个参数的构造函数来完成，每个参数表示一个依赖项

##### 构造函数参数解析

```xml
<!-- 构造函数参数解析 -->
    <bean id="simpleService" class="cn.yaolianhua.dependencies.constructor.SimpleService">
        <constructor-arg ref="simpleBean"/>
    </bean>
    <bean id="simpleBean" class="cn.yaolianhua.dependencies.constructor.SimpleBean"/>
```

```java
public class SimpleService {
    private final SimpleBean simpleBean;
    public SimpleService(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }
}
```
```java
public class SimpleBean {
	//others
}
```
##### 构造函数类型匹配

```xml
<bean id="exampleBean" class="cn.yaolianhua.dependencies.constructor.ExampleBean">
        <!-- 构造函数参数类型匹配 -->
        <constructor-arg type="int" value="2019"/>
        <constructor-arg type="java.lang.String" value="07-31"/>
</bean>
```
##### 构造函数参数索引

```xml
<bean id="exampleBean" class="cn.yaolianhua.dependencies.constructor.ExampleBean">
        <!-- 构造函数参数索引匹配 -->
        <constructor-arg index="0" value="2019"/>
        <constructor-arg index="1" value="07-31"/>
</bean>
```
##### 构造函数参数名称

```xml
<bean id="exampleBean" class="cn.yaolianhua.dependencies.constructor.ExampleBean">
        <!-- 构造函数参数名称匹配 -->
        <constructor-arg name="str" value="07-31"/>
        <constructor-arg name="num" value="2019"/>
</bean>
```

```java
public class ExampleBean {
    private int num;
    private String str;

    public ExampleBean(int num, String str) {
        this.num = num;
        this.str = str;
    }
}
```
#### setter注入

> 在调用无参数构造函数或无参数static工厂方法来实例化bean之后，基于setter的DI由bean上的容器调用setter方法完成

```xml
<!--  setter 注入-->
    <bean id="exampleBean2" class="cn.yaolianhua.dependencies.setter.ExampleBean2">
        <property name="simpleBean2" ref="simpleBean2"/>
        <property name="string" value="2019"/>
    </bean>
    <bean id="simpleBean2" class="cn.yaolianhua.dependencies.setter.SimpleBean2"/>
```

```java
public class ExampleBean2 {
    private SimpleBean2 simpleBean2;
    private String string;

    public void setSimpleBean2(SimpleBean2 simpleBean2) {
        this.simpleBean2 = simpleBean2;
    }
    public void setString(String string) {
        this.string = string;
    }
}
```
```java
public class SimpleBean2 {
}
```
#### 一个使用静态工厂方法注入的例子

> static工厂方法的参数由`<constructor-arg/>`元素提供，与实际使用的构造函数完全相同。工厂方法返回的类的类型不必与包含static工厂方法的类相同（尽管在本例中，它是）。实例（非静态）工厂方法可以以基本相同的方式使用（除了使用factory-bean属性而不是class属性）

```xml
<bean id="exampleBean3" class="cn.yaolianhua.dependencies.example.ExampleBean3" factory-method="getInstance">
        <constructor-arg ref="simpleBean3"/>
        <constructor-arg name="string" value="2019"/>
    </bean>
    <bean id="simpleBean3" class="cn.yaolianhua.dependencies.example.SimpleBean3"/>
```
```java
public class ExampleBean3 {

    private SimpleBean3 simpleBean3;
    private String string;

    private ExampleBean3(SimpleBean3 simpleBean3, String string) {
        this.simpleBean3 = simpleBean3;
        this.string = string;
    }

    public static ExampleBean3 getInstance(SimpleBean3 simpleBean3,String string){
        ExampleBean3 bean3 = new ExampleBean3(simpleBean3, string);
        //some other operations
        return bean3;
    }
}
```
```java
public class SimpleBean3 {
}
```
#### 循环依赖

> A类依赖B类，B类依赖A类

类A通过构造函数注入需要类B的实例，而类B通过构造函数注入需要类A的实例。如果将A类和B类的bean配置为相互注入，则Spring IoC容器会在运行时检测到此循环引用，并抛出 BeanCurrentlyInCreationException

**一种可行方案是通过setter注入**

```xml
<!-- setter 注入-->
    <bean id="circularBean2" class="cn.yaolianhua.dependencies.circular.CircularBean2">
        <property name="bean1" ref="circularBean1"/>
    </bean>
    <bean id="circularBean1" class="cn.yaolianhua.dependencies.circular.CircularBean1">
        <property name="bean2" ref="circularBean2"/>
    </bean>
```

```java
public class CircularBean1 {
    private CircularBean2 bean2;

    public void setBean2(CircularBean2 bean2) {
        this.bean2 = bean2;
    }
}

public class CircularBean2 {
    private CircularBean1 bean1;

    public void setBean1(CircularBean1 bean1) {
        this.bean1 = bean1;
    }
}
```

##### 容器执行bean依赖性解析

- 使用ApplicationContext描述所有bean的配置元数据创建和初始化。配置元数据可以由XML，Java代码或注释指定

- 对于每个bean，它的依赖关系以属性，构造函数参数或static-factory方法的参数的形式表示（如果使用它而不是普通的构造函数）。实际创建bean时，会将这些依赖项提供给bean

- 每个属性或构造函数参数都是要设置的值的实际定义，或者是对容器中另一个bean的引用

- 作为值的每个属性或构造函数参数都从其指定的格式转换为该属性或构造函数参数的实际类型。默认情况下，Spring能够转换成字符串格式提供给所有内置类型的值，例如int， long，String，boolean，等等

Spring容器在创建容器时验证每个bean的配置。但是，在实际创建bean之前，不会设置bean属性本身。创建容器时会创建单例作用域并设置为预先实例化（默认值）的Bean

如果不存在循环依赖关系，当一个或多个协作bean被注入依赖bean时，每个协作bean在被注入依赖bean之前完全配置。这意味着，如果bean A依赖于bean B，则Spring IoC容器在调用bean A上的setter方法之前完全配置bean B.换句话说，bean被实例化（如果它不是预先实例化的单例），设置其依赖项，并调用相关的生命周期方法

#### 如何选择

- 由于您可以混合基于构造函数和基于setter的DI，因此将构造函数用于强制依赖项和setter方法或可选依赖项的配置方法是一个很好的经验法则。请注意， 在setter方法上使用@Required注释可用于使属性成为必需的依赖项; 但是，最好使用编程验证参数的构造函数注入

- Spring团队通常提倡构造函数注入，因为它允许您将应用程序组件实现为不可变对象，并确保所需的依赖项不是null。此外，构造函数注入的组件始终以完全初始化的状态返回到客户端（调用）代码。作为旁注，大量的构造函数参数是一个糟糕的代码气味，暗示该类可能有太多的责任，应该重构以更好地解决关注点的正确分离

- Setter注入应主要仅用于可在类中指定合理默认值的可选依赖项。否则，必须在代码使用依赖项的任何位置执行非空检查。setter注入的一个好处是setter方法使该类的对象可以在以后重新配置或重新注入

- 使用对特定类最有意义的DI样式。有时，在处理您没有源的第三方类时，会选择您。例如，如果第三方类没有公开任何setter方法，那么构造函数注入可能是唯一可用的DI形式

### Dependencies and Configuration in Detail

#### Straight Values (Primitives, Strings, and so on)

一个数据源的连接的简要配置

```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://192.168.80.11:3306/bdpf"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
</bean>
<bean id="dataSourceExample" class="cn.yaolianhua.dependencies.configuration_detail.straight_values.DataSourceExample">
        <constructor-arg ref="dataSource"/>
</bean>

<!-- 使用p命名空间进行更简洁的XML配置 -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource"
          p:driverClassName="com.mysql.jdbc.Driver"
          p:url="jdbc:mysql://192.168.80.11:3306/bdpf"
          p:username="root"
          p:password="root"/>
<bean id="dataSourceExample" class="cn.yaolianhua.dependencies.configuration_detail.straight_values.DataSourceExample">
        <constructor-arg ref="dataSource"/>
</bean>
```

```java
public class DataSourceExample {
    private final DriverManagerDataSource dataSource;

    public DataSourceExample(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DriverManagerDataSource getDataSource() {
        return dataSource;
    }
}
```

#### 集合

> `<list/>`，`<set/>`，`<map/>`，和`<props/>`元素设置Java的属性和参数Collection类型List，Set，Map，和Properties

```xml
<import resource="straight-values.xml"/>
    <bean id="collectionExample" class="cn.yaolianhua.dependencies.configuration_detail.collection.CollectionExample">
        <constructor-arg name="properties">
            <props>
                <prop key="administrator">administrator@example.org</prop>
                <prop key="support">support@example.org</prop>
                <prop key="development">development@example.org</prop>
            </props>
        </constructor-arg>
        <constructor-arg name="list">
            <list>
                <value>a list element followed by a reference</value>
                <ref bean="dataSourceExample" />
            </list>
        </constructor-arg>
        <constructor-arg name="map">
            <map>
                <entry key="an entry" value="just some string"/>
                <entry key ="a ref" value-ref="dataSourceExample"/>
            </map>
        </constructor-arg>
        <constructor-arg name="set">
            <set>
                <value>just some string</value>
                <ref bean="dataSourceExample" />
            </set>
        </constructor-arg>
</bean>
```
```xml
public class CollectionExample {
    private final Properties properties;
    private final Map map;
    private final Set set;
    private final List list;

    public CollectionExample(Properties properties, Map map, Set set, List list) {
        this.properties = properties;
        this.map = map;
        this.set = set;
        this.list = list;
    }
}
```

映射键或值的值或设置值也可以是以下任何元素
```xml
bean | ref | idref | list | set | map | props | value | null
```

##### 集合的合并

> Spring容器还支持合并集合。应用程序开发人员可以定义父<list/>，<map/>，<set/>或<props/>元素，并有孩子`<list/>`，`<map/>`，`<set/>`或`<props/>`元素继承和父集合覆盖值。也就是说，子集合的值是合并父集合和子集合的元素的结果，子集合的元素覆盖父集合中指定的值

```xml
<bean id="parent" abstract="true" class="cn.yaolianhua.dependencies.configuration_detail.collection.CollectionMergingExample">
            <property name="properties">
                <props>
                    <prop key="administrator">administrator@example.com</prop>
                    <prop key="support">support@example.com</prop>
                </props>
            </property>
</bean>
<bean id="child" parent="parent">
	<property name="properties">
		<props merge="true">
			<prop key="sales">sales@example.com</prop>
			<prop key="support">support@example.co.uk</prop>
		</props>
	</property>
</bean>
```
```java
public class CollectionMergingExample {

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
```
#### 带有p命名空间的XML快捷方式

示例包括另外两个bean定义，它们都引用了另一个bean
```xml
<!-- 带有p命名空间的XML快捷方式 -->
    <bean name="john-classic" class="cn.yaolianhua.dependencies.configuration_detail.namespace.Pnamespace">
        <property name="name" value="John Doe"/>
        <property name="spouse" ref="jane"/>
    </bean>

    <bean name="john-modern"
          class="cn.yaolianhua.dependencies.configuration_detail.namespace.Pnamespace"
          p:name="John Doe"
          p:spouse-ref="jane"/>

    <bean name="jane" class="cn.yaolianhua.dependencies.configuration_detail.namespace.Pnamespace">
        <property name="name" value="Jane Doe"/>
    </bean>
```
```java
public class Pnamespace {
    private String name;
    private Pnamespace spouse;

    public void setName(String name) {
        this.name = name;
    }
    public void setSpouse(Pnamespace spouse) {
        this.spouse = spouse;
    }
}
```
此示例不仅包含使用p命名空间的属性值，还使用特殊格式来声明属性引用。第一个bean定义用于`<property name="spouse" ref="jane"/>`创建从bean `john`到bean  `jane`的引用，而第二个bean定义`p:spouse-ref="jane"`用作属性来执行完全相同的操作。在这种情况下，spouse是属性名称，而该-ref部分表示这不是直接值，而是对另一个bean的引用

#### 带有c命名空间的XML快捷方式

示例使用c:命名空间执行与`基于构造函数的依赖注入`相同的操作

```xml
<!-- 带有c命名空间的XML快捷方式(名称) -->
    <bean id="objectBean" class="cn.yaolianhua.dependencies.configuration_detail.namespace.ObjectBean"/>
    <bean id="cnamespace" class="cn.yaolianhua.dependencies.configuration_detail.namespace.Cnamespace"
          c:str1="string str"
          c:num="2019"
          c:obj-ref="objectBean"/>

    <!-- 带有c命名空间的XML快捷方式(索引) -->
    <bean id="cnamespace2" class="cn.yaolianhua.dependencies.configuration_detail.namespace.Cnamespace"
          c:_0="string 0"
          c:_1="2019"
          c:_2-ref="objectBean"/>
```
```java
public class Cnamespace {
    private String str1;
    private int num;
    private ObjectBean obj;

    public Cnamespace(String str1, int num, ObjectBean obj) {
        this.str1 = str1;
        this.num = num;
        this.obj = obj;
    }
}
```
```java
public class ObjectBean {
}
```

#### depends-on

> 如果bean是另一个bean的依赖项，那通常意味着将一个bean设置为另一个bean的属性。通常，您可以使用基于XML的配置元数据中的`<ref/> `元素来完成此操作。但是，有时bean之间的依赖关系不那么直接。例如，需要触发类中的静态初始化程序，例如数据库驱动程序注册。depends-on在初始化使用此元素的bean之前，该属性可以显式强制初始化一个或多个bean

```xml
<bean id="exampleBean" class="cn.yaolianhua.dependencies.configuration_detail.depends_on.ExampleBean"/>
    <bean id="dependsOnExample" class="cn.yaolianhua.dependencies.configuration_detail.depends_on.DependsOnExample"
          depends-on="exampleBean"
          p:exampleBean-ref="exampleBean"/>
```
```java
public class DependsOnExample {
    private ExampleBean exampleBean;

    public DependsOnExample() {
        System.out.println("DependsOnExample no param constructor");
    }

    public ExampleBean getExampleBean() {
        return exampleBean;
    }

    public void setExampleBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
```
```java
public class ExampleBean {
    public ExampleBean() {
        System.out.println("ExampleBean no param constructor");
        initProcess();
    }

    public static void initProcess(){
        System.out.println("do process ...");
    }
}
```

#### bean的懒加载

> 默认情况下，ApplicationContext实现会急切地创建和配置所有 单例 bean，作为初始化过程的一部分。通常，这种预先实例化是可取的，因为配置或周围环境中的错误是立即发现的，而不是几小时甚至几天后。当不希望出现这种情况时，可以通过将bean定义标记为延迟初始化来阻止单例bean的预实例化。延迟初始化的bean告诉IoC容器在第一次请求时创建bean实例，而不是在启动时

```xml
<bean id="exampleBean" class="cn.yaolianhua.dependencies.configuration_detail.depends_on.ExampleBean"/>
    <bean id="dependsOnExample" class="cn.yaolianhua.dependencies.configuration_detail.depends_on.DependsOnExample"
          lazy-init="true"
          depends-on="exampleBean"
          p:exampleBean-ref="exampleBean"/>
```

#### 自动装配

- 自动装配可以显着减少指定属性或构造函数参数的需要
- 自动装配可以随着对象的发展更新配置

使用基于XML的配置元数据时，可以使用元素`<bean/>`的`autowire`属性为 bean定义指定autowire模式

** 自动装配模式 **

- **no (默认）: **无自动装配。Bean引用必须由ref元素定义。不建议对较大的部署更改默认设置，因为明确指定协作者可以提供更好的控制和清晰度。在某种程度上，它记录了系统的结构

- **byName：** 按属性名称自动装配。Spring查找与需要自动装配的属性同名的bean。例如，如果bean定义按名称设置为autowire并且它包含一个master属性（即，它有一个 setMaster(..)方法），则Spring会查找名为master属性,并使用它来设置bean的定义

- **byType:** 如果容器中只存在一个属性类型的bean，则允许属性自动装配。如果存在多个，则抛出致命异常，这表示您可能不会byType对该bean 使用自动装配。如果没有匹配的bean，则不会发生任何事情（该属性未设置）

- **constructor:** 类似byType但适用于构造函数参数。如果容器中没有构造函数参数类型的一个bean，则会引发致命错误

##### 从自动装配中排除bean

在每个bean的基础上，您可以从自动装配中排除bean。在Spring的XML格式中，将元素`<bean/>`的`autowire-candidate`属性设置为false。容器使特定的bean定义对自动装配基础结构不可用

autowire-candidate属性旨在仅影响基于`类型`的自动装配。它不会影响`名称`的显式引用，即使指定的bean未标记为autowire候选，也会解析它。因此，如果名称匹配，则按名称自动装配会注入bean

#### 方法注入

> 在大多数应用程序场景中，容器中的大多数bean都是 单例。当单例bean需要与另一个单例bean协作或非单例bean需要与另一个非单例bean协作时，通常通过将一个bean定义为另一个bean的属性来处理依赖关系。当bean生命周期不同时会出现问题。假设单例bean A需要使用非单例（原型）bean B，可能是在A上的每个方法调用上。容器只创建一次单例bean A，因此只有一次机会来设置属性。每次需要时，容器都不能为bean A提供bean B的新实例

```xml
<bean id="scopeExampleBean" class="cn.yaolianhua.dependencies.configuration_detail.mi.ScopeExampleBean" scope="prototype"/>
    <!-- 侵入型太强，不推荐使用 -->
 <bean id="methodInjectionExample" class="cn.yaolianhua.dependencies.configuration_detail.mi.MethodInjectionExample" />
```
```java
public class MethodInjectionExample implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ScopeExampleBean getScopeExampleBean(){
        return this.applicationContext.getBean(ScopeExampleBean.class);
    }
}
```

##### 查找方法注入

> 查找方法注入是容器覆盖容器管理的bean上的方法并返回容器中另一个命名bean的查找结果的能力

```xml
<bean id="scopeExampleBean" class="cn.yaolianhua.dependencies.configuration_detail.mi.ScopeExampleBean" scope="prototype"/>
<!-- 推荐方式 -->
    <bean id="methodInjectionExample" class="cn.yaolianhua.dependencies.configuration_detail.mi.MethodInjectionExample2">
        <lookup-method name="getScopeExampleBean" bean="scopeExampleBean"/>
    </bean>
```
```java
public abstract class MethodInjectionExample2 {

    public abstract ScopeExampleBean getScopeExampleBean();
}
```
## Bean Scopes

Spring支持的6种bean的作用域，其中后面四个仅在web应用中得到支持

- **singleton：**（默认）将单个bean定义范围限定为每个Spring IoC容器的单个对象实例

- **prototype：**将单个bean定义范围限定为任意数量的对象实例

- **request：**将单个bean定义范围限定为单个HTTP请求的生命周期。也就是说，每个HTTP请求都有自己的bean实例，它是在单个bean定义的后面创建的

- **session：**将单个bean定义范围限定为一个HTTP Session的生命周期

- **application：**将单个bean定义范围限定为一个ServletContext的生命周期

- **websocket：**将单个bean定义范围限定为一个websocket的生命周期

### singleton scope

> 当您定义bean并将其作为单一作用域时，Spring IoC容器只创建该bean定义定义的对象的一个实例。此单个实例存储在此类单例bean的缓存中，并且该命名Bean的所有后续请求和引用都将返回缓存对象

[![](https://file.yaolh.cn/data/images/blog/spring/singleton.png)](https://file.yaolh.cn/data/images/blog/spring/singleton.png)

### prototype scope

> bean部署的非单例原型范围导致每次发出对该特定bean的请求时都创建新的bean实例。也就是说，bean被注入另一个bean，或者通过getBean()对容器的方法调用来请求它。通常，您应该对所有`有状态bean`使用原型范围，对`无状态bean`使用单例范围

[![](https://file.yaolh.cn/data/images/blog/spring/prototype.png)](https://file.yaolh.cn/data/images/blog/spring/prototype.png)

#### 补充

数据访问对象（DAO）通常不配置为原型，因为典型的DAO不会保持任何会话状态

与其他范围相比，Spring不管理原型bean的完整生命周期。容器实例化，配置和组装原型对象并将其交给客户端，而没有该原型实例的进一步记录。因此，尽管无论范围如何都在所有对象上调用初始化生命周期回调方法，但在原型的情况下，不会调用已配置的销毁生命周期回调。客户端代码必须清理原型范围的对象并释放原型bean所拥有的昂贵资源。要使Spring容器释放原型范围的bean所拥有的资源，请尝试使用自定义bean后处理器，它包含对需要清理的bean的引用

### 单例bean中依赖原型bean

当您使用具有依赖于原型bean的单例作用域bean时，请注意在实例化时解析依赖项。因此，如果依赖项将原型范围的bean注入到单例范围的bean中，则会实例化一个新的原型bean，然后将依赖注入到单例bean中。原型实例是唯一提供给单例范围bean的实例。

但是，假设您希望单例范围的bean在运行时重复获取原型范围的bean的新实例。您不能将原型范围的bean依赖注入到您的单例bean中，因为当Spring容器实例化单例bean并解析并注入其依赖项时，该注入只发生一次。如果您需要在运行时多次使用原型bean的新实例，请参阅方法注入

### 其他范围

参见[这里](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#beans-factory-scopes-other "这里")

## 自定义bean

Spring Framework提供了许多可用于自定义bean特性的接口

- 生命周期回调

- ApplicationContextAware and BeanNameAware

- Other Aware Interfaces

### 生命周期回调

```xml
<!-- 基于接口实现的生命周期回调 -->
    <bean class="cn.yaolianhua.dependencies.customBean.LifecycleCallbacksExample" p:string="a string value"/>

    <!-- 基于注解的生命周期回调 -->
    <context:annotation-config/>
    <bean class="cn.yaolianhua.dependencies.customBean.LifecycleCallbacksExampleWithAnnotation" p:string="a string value"/>

    <!-- 基于xml配置的生命周期回调-->
    <bean class="cn.yaolianhua.dependencies.customBean.LifecycleCallbacksExampleWithXml"
          c:string="a string value"
          init-method="init"
          destroy-method="destroy"/>
```

#### InitializingBean和DisposableBean接口

要与容器的bean生命周期管理进行交互，可以实现Spring InitializingBean和DisposableBean接口

```java
public class LifecycleCallbacksExample implements InitializingBean , DisposableBean {
    private String string;
    public LifecycleCallbacksExample() {
        System.out.println("LifecycleCallbacksExample Instantiation");
    }

    public void setString(String string) {
        this.string = string;
        System.out.println("LifecycleCallbacksExample properties set["+string+"]");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet invoked");
    }

    public void destroy() throws Exception {
        System.out.println("destroy invoked");
    }
}
```

#### @PostConstruct和@PreDestroy

```java
public class LifecycleCallbacksExampleWithAnnotation{
    private String string;
    public LifecycleCallbacksExampleWithAnnotation() {
        System.out.println("LifecycleCallbacksExampleWithAnnotation Instantiation");
    }

    public void setString(String string) {
        this.string = string;
        System.out.println("LifecycleCallbacksExampleWithAnnotation properties set["+string+"]");
    }
    @PostConstruct
    public void myInit(){
        System.out.println("myInit invoked ");
    }

    @PreDestroy
    public void myDestroy(){
        System.out.println("myDestroy invoked");
    }
}
```
#### 自定义方法

```java
public class LifecycleCallbacksExampleWithXml {
    private String string;

    public LifecycleCallbacksExampleWithXml(String string) {
        this.string = string;
    }
    public void init(){
        System.out.println("custom init invoked");
    }
    public void destroy(){
        System.out.println("custome destroy invoked");
    }
}
```
### ApplicationContextAware and BeanNameAware

参见[这里](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#beans-factory-aware "这里")

### Other Aware Interfaces

参见[这里](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aware-list "这里")

## IoC 扩展点

通常，应用程序开发人员不需要子类化ApplicationContext 实现类。相反，可以通过插入特殊集成接口的实现来扩展Spring IoC容器

### BeanPostProcessor

> BeanPostProcessor接口定义了您可以实现的回调方法，以提供您自己的（或覆盖容器的默认）实例化逻辑，依赖关系解析逻辑等。如果要在Spring容器完成实例化，配置和初始化bean之后实现某些自定义逻辑，则可以插入一个或多个自定义BeanPostProcessor实现

您可以配置多个BeanPostProcessor实例，并且可以通过设置order属性来控制这些实例的执行顺序。只有在BeanPostProcessor实现Ordered 接口时才能设置此属性。如果你自己编写BeanPostProcessor，你也应该考虑实现这个Ordered接口

#### 一个简单的增强处理

```java
public interface MyService {
    void service();
}

public class ExampleBean implements MyService{

    public void service() {
        System.out.println("do service");
    }

}
```
```java
public class BeanPostProcessorExample implements BeanPostProcessor, Ordered {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("exampleBean")){
            //do something
            return bean;
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("exampleBean")){
            Object proxyInstance = Proxy.newProxyInstance(BeanPostProcessorExample.class.getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new ExampleBeanHandler(bean));
            System.out.println("BeanPostProcessorExample postProcessAfterInitialization ["+proxyInstance.getClass()+"]");

            return proxyInstance;
        }
        return bean;
    }

    public int getOrder() {
        return 0;
    }
}
```
```java
public class ExampleBeanHandler implements InvocationHandler {

    private Object target;
    public ExampleBeanHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("... before ...");
        Object invoke = method.invoke(target, args);
        System.out.println("... after ...");

        return invoke;
    }
}
```
结果清单
```java
BeanPostProcessorExample postProcessAfterInitialization [class com.sun.proxy.$Proxy8]
myService instanceof Proxy ? true
... before ...
do service
... after ...
```

### BeanFactoryPostProcessor

> 这个接口的语义类似于BeanPostProcessor它的一个主要区别：BeanFactoryPostProcessor操作bean配置元数据。也就是说，Spring IoC容器允许BeanFactoryPostProcessor读取配置元数据，并可能在容器实例化之前更改除BeanFactoryPostProcessor 实例之外的任何bean

您可以配置多个BeanFactoryPostProcessor实例，并且可以BeanFactoryPostProcessor通过设置order属性来控制这些实例的运行顺序。但是，如果BeanFactoryPostProcessor实现 Ordered接口，则只能设置此属性。如果你自己编写BeanFactoryPostProcessor，你也应该考虑实现这个Ordered接口

#### 通过编程更改bean的作用域

使用上面例子

```java
public class BeanFactoryPostProcessorExample implements BeanFactoryPostProcessor, Ordered {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("exampleBean");
        beanDefinition.setScope("prototype");
    }

    public int getOrder() {
        return 0;
    }
}
```
结果清单

```java
BeanPostProcessorExample postProcessBeforeInitialization 
BeanPostProcessorExample postProcessAfterInitialization [class com.sun.proxy.$Proxy8]
cn.yaolianhua.dependencies.extensionPoints.ExampleBean@5158b42f
BeanPostProcessorExample postProcessBeforeInitialization 
BeanPostProcessorExample postProcessAfterInitialization [class com.sun.proxy.$Proxy8]
cn.yaolianhua.dependencies.extensionPoints.ExampleBean@595b007d
```

## 基于注释的配置

基于注释的配置的引入引发了这种方法是否比XML“更好”的问题。简短的回答是“它取决于。”长期的答案是每种方法都有其优点和缺点，通常，由开发人员决定哪种策略更适合他们。由于它们的定义方式，注释在其声明中提供了大量上下文，从而导致更短更简洁的配置。但是，XML擅长在不触及源代码或重新编译它们的情况下连接组件。一些开发人员更喜欢将布线靠近源，而另一些开发人员则认为注释类不再是POJO，而且配置变得分散且难以控制

基于注释的配置提供了XML设置的替代方案，该配置依赖于字节码元数据来连接组件而不是角括号声明。开发人员不是使用XML来描述bean连接，而是通过在相关的类，方法或字段声明上使用注释将配置移动到组件类本身。如示例中所述：RequiredAnnotationBeanPostProcessor使用BeanPostProcessor与注释结合使用是扩展Spring IoC容器的常用方法。例如，Spring 2.0引入了使用@Required注释强制执行所需属性的可能性。Spring 2.5使得有可能采用相同的通用方法来驱动Spring的依赖注入。基本上，@Autowired注释提供与自动装配协作者中描述的相同的功能，但具有更细粒度的控制和更广泛的适用性。Spring 2.5还增加了对JSR-250注释的支持，例如 @PostConstruct和@PreDestroy。Spring 3.0增加了对javax.inject包中包含的JSR-330（Java的依赖注入）注释的支持，例如@Inject 和@Named

`注释注入在XML注入之前执行。因此，XML配置会覆盖通过这两种方法连接的属性的注释`

与往常一样，您可以将它们注册为单独的bean定义，但也可以通过在基于XML的Spring配置中包含以下标记来隐式注册它们

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:c="http://www.springframework.org/schema/c"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBean" c:string="ExampleBean string value"/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBean2" c:string="ExampleBean2 string value"/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBean3" c:str="ExampleBean3 string value"/>
    <bean class="cn.yaolianhua.dependencies.annotation.AnnotationAutowiredExample"/>
    <bean class="cn.yaolianhua.dependencies.annotation.AnnotationRequiredExample"/>

    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl" c:string="ExampleBeanImpl string value [primary]" primary="true"/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl2" c:string="ExampleBeanImpl2 string value"/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl3" c:string="ExampleBeanImpl3 string value [qualifier main]">
        <qualifier value="main"/>
    </bean>
    <!--<bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl4" c:string="ExampleBeanImpl4 string value [qualifier action]">-->
        <!--<qualifier value="action"/>-->
    <!--</bean>-->
    <bean name="example5" class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl5" c:string="ExampleBeanImpl5 string value [resource example5]"/>
    <!--<bean id="example6" class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl6" c:string="ExampleBeanImpl6 string value [resource example6]"/>-->
    <bean class="cn.yaolianhua.dependencies.annotation.AnnotationResourceExample"/>
    <bean class="cn.yaolianhua.dependencies.annotation.AnnotationPrimaryExample"/>
    <bean class="cn.yaolianhua.dependencies.annotation.AnnotationQualifierExample"/>
</beans>
```
在隐式注册后处理器包括 AutowiredAnnotationBeanPostProcessor， CommonAnnotationBeanPostProcessor， PersistenceAnnotationBeanPostProcessor， RequiredAnnotationBeanPostProcessor

### Required

`@Required`注释适用于bean属性setter方法 `从Spring Framework 5.1开始，@Required注释正式被弃用`,这里无法完成注入

```java
public class AnnotationRequiredExample {
    private ExampleBean exampleBean;

//    @Required
//    public void setExampleBean(ExampleBean exampleBean) {
//        this.exampleBean = exampleBean;
//    }//NullPointerException
}
```

### Autowired

可以使用JSR 330的@Inject注释代替Spring的@Autowired注释

从Spring Framework 4.3开始，@Autowired如果目标bean只定义了一个开头的构造函数，则不再需要对这样的构造函数进行注释。但是，如果有几个构造器可用，则必须注释至少一个构造器以教导容器使用哪一个

```java
public class AnnotationAutowiredExample {
    @Autowired//将@Autowired注释应用于属性字段
    private ExampleBean exampleBean;
    private ExampleBean2 exampleBean2;
    private ExampleBean3 exampleBean3;

    @Autowired//将@Autowired注释应用于构造函数
    public AnnotationAutowiredExample(ExampleBean2 exampleBean2) {
        this.exampleBean2 = exampleBean2;
    }
    @Autowired//将@Autowired注释应用于setter方法
    public void setExampleBean3(ExampleBean3 exampleBean3) {
        this.exampleBean3 = exampleBean3;
    }

    public ExampleBean getExampleBean() {
        return exampleBean;
    }

    public ExampleBean2 getExampleBean2() {
        return exampleBean2;
    }

    public ExampleBean3 getExampleBean3() {
        return exampleBean3;
    }
}
```

### Primary

> 由于按类型自动装配可能会导致多个候选人，因此通常需要对选择过程进行更多控制。实现这一目标的一种方法是使用Spring的 @Primary注释。@Primary表示当多个bean可以自动装配到单值依赖项时，应该优先选择特定的bean。如果候选者中只存在一个主bean，则它将成为自动装配的值

这里基于xml配置

```xml
<bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl" c:string="ExampleBeanImpl string value [primary]" primary="true"/>
    <bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl2" c:string="ExampleBeanImpl2 string value"/>
```
```java
public class AnnotationPrimaryExample {
    @Autowired
    private ExampleBeanPrimaryInterface exampleBeanPrimaryInterface;
}
public interface ExampleBeanPrimaryInterface {
    String getString();
}
```
### Qualifier

> @Primary当可以确定一个主要候选者时，是通过具有多个实例的类型使用自动装配的有效方式。当您需要更多控制选择过程时，可以使用Spring的@Qualifier注释。您可以将限定符值与特定参数相关联，缩小类型匹配集，以便为每个参数选择特定的bean

```xml
<bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl3" c:string="ExampleBeanImpl3 string value [qualifier main]">
        <qualifier value="main"/>
</bean>
<bean class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl4" c:string="ExampleBeanImpl4 string value [qualifier action]">
        <qualifier value="action"/>
</bean>
```
```java
public class AnnotationQualifierExample {
    @Autowired
    @Qualifier(value = "main")
    private ExampleBeanQualifierInterface exampleBeanQualifierInterface;
}
public interface ExampleBeanQualifierInterface {
    String getString();
}
```
根据官方配置，本地测试未通过`expected single matching bean but found 2`

### Resource

> Spring还通过在字段或bean属性setter方法上使用JSR-250 @Resourceannotation（javax.annotation.Resource）来支持注入

```xml
<bean name="example5" class="cn.yaolianhua.dependencies.annotation.ExampleBeanImpl5" c:string="ExampleBeanImpl5 string value [resource example5]"/>
```
```java
public class AnnotationResourceExample {
    @Resource(name = "example5")
    private ExampleBeanResourceInterface exampleBeanResourceInterface;
}
public interface ExampleBeanResourceInterface {
    String getString();
}
```

## 类路径扫描和托管组件

参见[这里](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#beans-classpath-scanning "这里")

## 基于Java的配置

> Spring的新Java配置支持中的中心工件是 @Configuration注释类和@Bean注释方法

### 基本概念：Bean和Configuration

> @Bean注释被用于指示一个方法实例，配置和初始化为通过Spring IoC容器进行管理的新对象。对于那些熟悉Spring的<beans/>XML配置的人来说，@Bean注释与<bean/>元素扮演的角色相同。你可以在任何Spring @Component中使用@Bean-annotated方法。但是，它们最常用于@Configuration beans

对类进行注释@Configuration表明其主要目的是作为bean定义的来源。此外，@Configuration类允许通过调用@Bean同一类中的其他方法来定义bean间依赖关系。最简单的@Configuration类如下

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```
AppConfig类等效于以下Spring `<beans/>`XML

```xml
<bean id="myService" class="com.acme.services.MyServiceImpl"/>
```
#### Full @Configuration vs “lite” @Bean mode
当@Bean方法在没有使用@Configuration注释的类中声明时，它们被称为在“lite”模式下处理。在@Component中声明的Bean方法，甚至在普通的旧类中声明的Bean方法，都被认为是“lite”，包含类的主要目的不同，而@Bean方法在这里是一种附加功能。例如，服务组件可以通过每个适用组件类上的附加@Bean方法向容器公开管理视图。在这种情况下，@Bean方法是一种通用的工厂方法机制。

与完整的@Configuration不同，lite @Bean方法不能声明bean之间的依赖关系。相反，它们对包含它们的组件的内部状态进行操作，并可选地对它们可能声明的参数进行操作。因此，这样的@Bean方法不应该调用其他@Bean方法。每个这样的方法实际上只是一个特定bean引用的工厂方法，没有任何特殊的运行时语义。这里的积极副作用是在运行时不需要应用CGLIB子类，所以在类设计方面没有限制(也就是说，包含的类可能是final类，等等)。

在常见的场景中，@Bean方法将在@Configuration类中声明，以确保始终使用“full”模式，并因此将交叉方法引用重定向到容器的生命周期管理。这可以防止通过常规Java调用意外调用相同的@Bean方法，这有助于减少在“lite”模式下操作时难以跟踪的细微bug

#### 实例化IoC容器

当@Configuration类作为输入提供时，@Configuration类本身注册为bean定义，类中所有声明的@Bean方法也注册为bean定义

当提供@Component和JSR-330类时，它们被注册为bean定义，并且假设DI元数据(如@Autowired或@Inject)在这些类中使用

```java
private static void buildIoC(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();
        context.getBean(A.class).doSomething();
    }
```
 ### 使用Bean
 @Bean是方法级别的注释，是XML 元素的直接模拟。注释支持提供的一些属性，比如:* init-method * destroy-method * autowiring * name

您可以在@Configuration-annotated类或@Component-annotated类中使用@Bean注释

#### 声明一个Bean

要声明bean，可以用@Bean注释方法。您可以使用此方法在作为方法返回值指定的类型的ApplicationContext中注册bean定义。默认情况下，bean名称与方法名称相同

```java
@Configuration
@ComponentScan(basePackages = "cn.yaolianhua.dependencies.javaBase")
public class AppConfig {

//    @Bean
//    public B b(){
//        return new B();
//    }
//或者基于接口（或者基类）
    @Bean
    public C c(){
        return new B();
    }
	//或者引用其他bean
    @Bean
    public D d(){
        return new D(c());
    }
}
```
#### 生命周期回调

使用@Bean注释定义的任何类都支持常规的生命周期回调，并且可以使用JSR-250中的@PostConstruct和@PreDestroy注释

还完全支持常规的Spring生命周期回调。如果bean实现了InitializingBean、DisposableBean 或Lifecycle ，容器将调用它们各自的方法

还完全支持标准的*Aware 接口集(如BeanFactoryAware、BeanNameAware、MessageSourceAware、applicationcontext - ware等)

@Bean注释支持指定任意初始化和销毁回调方法，很像Spring XML在bean元素上的init-method和destroy-method属性，如下面的示例所示

```java
@Configuration
@ComponentScan(basePackages = "cn.yaolianhua.dependencies.javaBase")
public class AppConfig {

    @Bean(initMethod = "init")
    public B b(){
        return new B();
    }
    @Bean(destroyMethod = "destroy")
    public D d(){
        return new D(b());
    }
}
public class B implements C{
    public void init(){
        System.out.println(" B init invoked");
    }
}
public class D{
    public void destroy(){
        System.out.println("D destroy invoked");
    }
}
```
#### bean的作用域

```java
@Bean
@Scope("prototype")
	public E e(){
		return new E();
}
```
#### 自定义命名

```java
    @Bean("eName")
    @Scope("prototype")
    public E e(){
        return new E();
    }
```

### 使用Configuration

> @Configuration是一个类级注释，指示对象是bean定义的源。@Configuration类通过公共的@Bean注释方法声明bean。在@Configuration类上调用@Bean方法也可以用来定义bean之间的依赖关系

#### 注入bean的依赖关系

> 这种声明bean间依赖关系的@Bean方法只有在@Configuration类中声明方法时才有效。您不能使用普通@Component类声明bean间依赖项

```java
@Configuration
public class AppConfig {

    @Bean
    public BeanOne beanOne() {
        return new BeanOne(beanTwo());
    }

    @Bean
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

#### 查找方法注入

在单例范围的bean依赖于原型范围的bean的情况下，它很有用

```java
    @Bean
    @Scope("prototype")
    public CommandImpl commandImpl(){
        return new CommandImpl();
    }

    @Bean
    public CommandManager commandManager(){
        return new CommandManager() {
            @Override
            protected Command createCommand() {
                return commandImpl();
            }
        };
    }

public abstract class CommandManager {
    protected abstract Command createCommand();
}
```

### 编写基于Java的配置

Spring的基于Java的配置功能允许您撰写注释，这可以降低配置的复杂性

#### 使用@Import

就像`<import/>`在Spring XML文件中使用该元素来帮助模块化配置一样，@Import注释允许@Bean从另一个配置类加载定义

```java
@Configuration
public class ConfigA {

    @Bean
    public A a() {
        return new A();
    }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public B b() {
        return new B();
    }
}
```
现在，不需要同时指定ConfigA.class和ConfigB.class实例化上下文，只ConfigB需要显式提供

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

    // now both beans A and B will be available...
    A a = ctx.getBean(A.class);
    B b = ctx.getBean(B.class);
}
```

##### 在导入的@Bean定义上注入依赖项

前面的例子有效，但过于简单。在大多数实际场景中，bean在配置类之间相互依赖。当使用XML时，这不是问题，因为不涉及编译器，您可以声明ref="someBean"，并信任Spring在容器初始化期间解决它。当使用@Configuration类时，Java编译器会对配置模型施加约束，因为对其他bean的引用必须是有效的Java语法

幸运的是，解决这个问题很简单。正如我们已经讨论过的，@Bean方法可以有任意数量的参数来描述bean依赖关系

```java
@Configuration
public class ServiceConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}
```

还有另一种方法可以达到同样的效果。请记住，@Configuration类最终只是容器中的另一个bean:这意味着它们可以利用@Autowired和@Value注入以及与任何其他bean相同的其他特性

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    private final DataSource dataSource;

    @Autowired
    public RepositoryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}
```

#### 结合Java和xml的使用

Spring的@Configuration类支持并非旨在成为Spring XML的100％完全替代品。某些工具（如Spring XML命名空间）仍然是配置容器的理想方法。在XML方便或必要的情况下，您可以选择：例如，通过使用“以XML为中心”的方式实例化容器，或者通过使用AnnotationConfigApplicationContext和@ImportResource注释以“以Java为中心”的方式实例化它

##### 以XML为中心的@Configuration类的使用

最好从XML引导Spring容器并@Configuration以ad-hoc方式包含 类。例如，在使用Spring XML的大型现有代码库中，可以@Configuration根据需要更轻松地创建类，并将其包含在现有XML文件中

```java
@Configuration
public class XmlAndJava {

    @Resource
    private DataSource dataSourceXml;

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSourceXml);
    }

}
```
```xml
<context:annotation-config/>
    <!--<context:property-placeholder location="classpath:/javaBase/properties"/>-->
    <bean id="dataSourceXml" class="org.springframework.jdbc.datasource.DriverManagerDataSource"
          p:driverClassName="com.mysql.jdbc.Driver"
          p:url="jdbc:mysql://192.168.80.11:3306/bdpf"
          p:username="root"
          p:password="root"/>
    <bean class="cn.yaolianhua.dependencies.javaBase.XmlAndJava"/>
```

```java
ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext("classpath:javaBase/datasource.xml");
        System.out.println(context1.getBean(JdbcTemplate.class).hashCode());
```

##### 以@Configuration类为中心的XML使用

在@Configuration类是配置容器的主要机制的应用程序中，仍然可能需要使用至少一些XML。在这些场景中，您可以@ImportResource根据需要使用和定义尽可能多的XML。这样做可以实现“以Java为中心”的方法来配置容器并将XML保持在最低限度

```java
@Configuration
@ImportResource("classpath:/javaBase/properties-config.xml")
public class JavaAndXml {
    @Value("${url}")
    private String url;
    @Value("${username}")
    private String userName;
    @Value("${password}")
    private String password;
    @Value("${driverClassName}")
    private String driverClassName;

    @Bean("myDatasource")
    public DataSource dataSource(){
        return new DriverManagerDataSource(url,userName,password);
    }
}
```

properties-config.xml

```xml
<context:property-placeholder location="classpath:/javaBase/properties"/>
```

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaAndXml.class);
        System.out.println(context.getBean("myDatasource").hashCode());
```

### 有条件的使用Conditional

基于某些任意系统状态，有条件地启用或禁用完整@Configuration类或甚至单个@Bean方法通常很有用。一个常见的例子是@Profile只有在Spring中启用了特定的配置文件时才使用注释激活bean Environment

#### Profile的实现

```java
@Override
public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    if (context.getEnvironment() != null) {
        // Read the @Profile annotation attributes
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
        if (attrs != null) {
            for (Object value : attrs.get("value")) {
                if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
                    return true;
                }
            }
            return false;
        }
    }
    return true;
}
```

#### 自定义一个配置是否启用的实现

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Conditional(CustomCondition.class)
public @interface Flag {
    boolean value() default true;
}
```

```java
public class CustomCondition implements Condition {
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(Flag.class.getName());
        if (attributes != null){
            Object value = attributes.get("value");
            return Boolean.parseBoolean(value.toString());
        }

        return true;
    }
}
```
```java
@Component
@Flag(value = false)
public class A {
    public void doSomething(){
        System.out.println("A doSomething()");
    }
}
```
类A将不会被Spring容器注册