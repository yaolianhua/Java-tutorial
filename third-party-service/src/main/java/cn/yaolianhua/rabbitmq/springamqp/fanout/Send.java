package cn.yaolianhua.rabbitmq.springamqp.fanout;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 20:51
 **/
public class Send {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private  FanoutExchange fanout;

    AtomicInteger ds = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);
    @Scheduled(fixedDelay = 1000,initialDelay = 500)
    public void send(){
        StringBuilder sb = new StringBuilder("Hello");
        if (ds.getAndIncrement() == 3)
            ds.set(1);
        for (int i = 0; i < ds.get(); i++)
            sb.append('.');
        sb.append(count.incrementAndGet());
        final String message = sb.toString();
        template.convertAndSend(fanout.getName(),"",message);
        System.out.println(String.format("[x] Sent %s",message));
    }
}
