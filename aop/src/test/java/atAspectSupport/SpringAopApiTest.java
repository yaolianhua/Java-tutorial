package atAspectSupport;
import cn.yaolianhua.api.MyService;
import cn.yaolianhua.api.NoImplService;
import cn.yaolianhua.api.Service;
import cn.yaolianhua.atAspectSupport.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-11 22:58
 **/
public class SpringAopApiTest {

    public static void main(String[] args) {

        methodInterceptor();
    }

    private static void methodInterceptor(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        System.out.println(context.getBean(Service.class).process("a string"));
//        context.getBean(NoImplService.class).noImpl("a string");
        context.getBean(Service.class).throwing();
    }

}
