package cn.yaolianhua.rabbitmq.springamqp.helloworld;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 15:19
 **/
@RabbitListener(queues = "hello")
public class Receiver {
    @RabbitHandler
    public void receive(String msg){
        System.out.println(String.format("[x] received %s",msg));
    }
}
