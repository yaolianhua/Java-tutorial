package cn.yaolianhua.rabbitmq.springamqp.helloworld;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 15:11
 **/
@Configuration
@Profile({"helloworld","hello-world"})
public class HelloWorldConfig {

    @Bean
    public Queue queue(){
        return new Queue("hello",false,false,false,null);
    }

    @Bean
    @Profile("send")
    public Send send(){
        return new Send();
    }
    @Bean
    @Profile("receiver")
    public Receiver receiver(){
        return new Receiver();
    }
}
