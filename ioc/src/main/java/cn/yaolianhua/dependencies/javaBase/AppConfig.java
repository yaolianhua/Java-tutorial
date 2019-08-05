package cn.yaolianhua.dependencies.javaBase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 10:42
 **/
@Configuration
@ComponentScan(basePackages = "cn.yaolianhua.dependencies.javaBase")
public class AppConfig {

    @Bean(initMethod = "init")
    public B b(){
        return new B();
    }
//    @Bean
//    public C c(){
//        return new B();
//    }

    @Bean(destroyMethod = "destroy")
    public D d(){
        return new D(b());
    }
    @Bean("eName")
    @Scope("prototype")
    public E e(){
        return new E();
    }

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
}
