package cn.yaolianhua.atAspectSupport;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:20
 **/
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "cn.yaolianhua")
public class AppConfig {
}
