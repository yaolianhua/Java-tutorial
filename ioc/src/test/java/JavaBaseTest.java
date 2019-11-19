import cn.yaolianhua.dependencies.javaBase.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 10:44
 **/
public class JavaBaseTest {
    public static void main(String[] args) {
        System.out.println("** buildIoC **");
        buildIoC();
        System.out.println("** datasource **");
        datasource();
    }

    private static void buildIoC(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();
        context.getBean(A.class).doSomething();
        context.getBean(B.class).doSomething();
        context.getBean(C.class).doProcess();
        context.getBean(D.class).doSomething();
        System.out.println(context.getBean("eName").hashCode());
        System.out.println(context.getBean("eName").hashCode());
        System.out.println("commandManager hashcode:"+context.getBean(CommandManager.class).hashCode());
        System.out.println("CommandImpl hashcode:"+context.getBean(CommandImpl.class).hashCode());
        System.out.println("commandManager hashcode:"+context.getBean(CommandManager.class).hashCode());
        System.out.println("CommandImpl hashcode:"+context.getBean(CommandImpl.class).hashCode());



        context.close();
    }

    private static void datasource(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaAndXml.class);
        System.out.println(context.getBean("myDatasource").hashCode());

        ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext("classpath:javaBase/datasource.xml");
        System.out.println(context1.getBean(JdbcTemplate.class).hashCode());
    }

}
