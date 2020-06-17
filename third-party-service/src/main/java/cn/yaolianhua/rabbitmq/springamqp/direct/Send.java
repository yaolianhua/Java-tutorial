package cn.yaolianhua.rabbitmq.springamqp.direct;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 22:13
 **/
public class Send {

    private RabbitTemplate template;
    private DirectExchange direct;
    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }
    @Autowired
    public void setDirect(DirectExchange direct) {
        this.direct = direct;
    }

    AtomicInteger index = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    private final String[] keys = {"orange", "black", "green"};

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder sb = new StringBuilder("Hello to ");
        if (this.index.incrementAndGet() == 3) {
            this.index.set(0);
        }
        String key = keys[this.index.get()];
        sb.append(key).append(' ');
        sb.append(this.count.incrementAndGet());
        String message = sb.toString();
        template.convertAndSend(direct.getName(), key.toUpperCase(), message);
        System.out.println(String.format("[x] Sent %s",message));
    }
}
