package cn.yaolianhua.rabbitmq.springamqp.workqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 18:04
 **/
@Configuration
@Profile({"work-queues"})
public class WorkQueueConfig {
    @Bean
    public Queue queue(){
        return new Queue("task_queue",true,false,false,null);
    }
    @Profile("receiver")
    static class ReceiverConfig{
        @Bean
        public Receiver receiver1(){
            return new Receiver(1);
        }
        @Bean
        public Receiver receiver2(){
            return new Receiver(2);
        }
    }
    @Bean
    @Profile("send")
    public Send send(){
        return new Send();
    }
}
