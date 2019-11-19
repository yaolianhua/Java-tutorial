package cn.yaolianhua.atAspectSupport;

import cn.yaolianhua.api.DebugInterceptor;
import cn.yaolianhua.api.MyBeforeAdvice;
import cn.yaolianhua.api.RuntimeThrowsAdvice;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:20
 **/
@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableAspectJAutoProxy//default with implement interface,jdk proxy
@ComponentScan(basePackages = "cn.yaolianhua")
public class AppConfig {

    @Bean
    public RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor(){
        RegexpMethodPointcutAdvisor pointcut = new RegexpMethodPointcutAdvisor();
        pointcut.setPatterns(".*process",".*noImpl");
//        pointcut.setAdvice(new DebugInterceptor());
//        pointcut.setAdvice(new MyBeforeAdvice());
        pointcut.setAdvice(new RuntimeThrowsAdvice());
        return pointcut;
    }


}
