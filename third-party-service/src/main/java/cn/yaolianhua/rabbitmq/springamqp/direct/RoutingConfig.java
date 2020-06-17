package cn.yaolianhua.rabbitmq.springamqp.direct;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 22:13
 **/
@Configuration
@Profile("routing")
public class RoutingConfig {
    enum RoutingKey{ ORANGE, BLACK,GREEN}
    @Bean
    public DirectExchange direct(){
        return new DirectExchange("direct_logs",false,false,null);
    }
    @Profile("receiver")
    static class ReceiverConfig{
        @Bean
        public Queue autoDeleteQueue1(){
            return new AnonymousQueue();
        }
        @Bean
        public Queue autoDeleteQueue2(){
            return new AnonymousQueue();
        }
        @Bean
        public Binding bindingA1(DirectExchange direct, Queue autoDeleteQueue1){
            return BindingBuilder.bind(autoDeleteQueue1).to(direct).with(RoutingKey.ORANGE);
        }
        @Bean
        public Binding bindingA2(DirectExchange direct,Queue autoDeleteQueue1){
            return BindingBuilder.bind(autoDeleteQueue1).to(direct).with(RoutingKey.BLACK);
        }

        @Bean
        public Binding bindingB1(DirectExchange direct, Queue autoDeleteQueue2){
            return BindingBuilder.bind(autoDeleteQueue2).to(direct).with(RoutingKey.GREEN);
        }
        @Bean
        public Binding bindingB2(DirectExchange direct,Queue autoDeleteQueue2){
            return BindingBuilder.bind(autoDeleteQueue2).to(direct).with(RoutingKey.BLACK);
        }
        @Bean
        public Receiver receiver(){
            return new Receiver();
        }
    }

    @Bean
    @Profile("send")
    public Send send(){
        return new Send();
    }
}
