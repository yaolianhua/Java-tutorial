package cn.yaolianhua.rabbitmq.springamqp.topic;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 22:47
 **/
public class Send {
    private RabbitTemplate template;
    private TopicExchange topic;
    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }
    @Autowired
    public void setDirect(TopicExchange topic) {
        this.topic = topic;
    }

    AtomicInteger index = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    final String[] keys = {
            "quit.orange.rabbit",
            "lazy.orange.elephant",
            "quick.orange.fox",
            "lazy.brown.fox",
            "lazy.pink.rabbit",
            "quick.brown.fox"
    };

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder sb = new StringBuilder("Hello to ");
        if (this.index.incrementAndGet() == keys.length) {
            this.index.set(0);
        }
        String key = keys[this.index.get()];
        sb.append(key).append(' ');
        sb.append(this.count.incrementAndGet());
        String message = sb.toString();
        template.convertAndSend(topic.getName(), key, message);
        System.out.println(String.format("[x] Sent %s",message));
    }
}
