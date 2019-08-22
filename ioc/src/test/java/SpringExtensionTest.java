import cn.yaolianhua.extension.AppConfig;
import cn.yaolianhua.extension.atImport.ProxyCreator;
import cn.yaolianhua.extension.biz.ServiceExample;
import cn.yaolianhua.extension.biz.ServiceExample2;
import cn.yaolianhua.extension.biz.ServiceExample3;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-16 15:42
 **/
public class SpringExtensionTest {


    public static void main(String[] args) {
        proxy();
    }


    private static void proxy() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("print **** "+context.getBean(ServiceExample3.class).process3("java"));

        System.out.println("print **** "+((ServiceExample) context.getBean("serviceExampleImpl")).process("hello", "world"));

        System.out.println("print **** "+context.getBean(ServiceExample2.class).process(new Date()));
    }
}
