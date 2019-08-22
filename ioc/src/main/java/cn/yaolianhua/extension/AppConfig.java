package cn.yaolianhua.extension;

import cn.yaolianhua.extension.annotation.MyProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-16 15:39
 **/
@Configuration
@ComponentScan("cn.yaolianhua.extension")
//@MyProxy(value = false)
@MyProxy
public class AppConfig {

}
