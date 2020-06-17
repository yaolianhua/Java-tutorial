package cn.yaolianhua.rabbitmq.springamqp.workqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 17:49
 **/
public class Send {
    private RabbitTemplate template;
    private Queue queue;

    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    AtomicInteger dots = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        if (dots.getAndIncrement() == 4) {
            dots.set(1);
        }
        for (int i = 0; i < dots.get(); i++) {
            builder.append('.');
        }
        builder.append(count.incrementAndGet());
        String message = builder.toString();
        template.convertAndSend(queue.getName(), message);
        System.out.println(String.format("[x] Sent %s", message));
    }
}
