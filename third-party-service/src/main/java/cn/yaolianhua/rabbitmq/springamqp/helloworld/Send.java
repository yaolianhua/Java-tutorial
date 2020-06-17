package cn.yaolianhua.rabbitmq.springamqp.helloworld;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 15:13
 **/
public class Send {
    private  RabbitTemplate template;
    private  Queue queue;
    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }
    @Autowired
    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    @Scheduled(fixedDelay = 1000,initialDelay = 500)
    public void send(){
        String message = "Hello World";
        template.convertAndSend(queue.getName(),message);
        System.out.println(String.format("[x] Sent %s",message));
    }
}
