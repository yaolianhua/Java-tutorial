package cn.yaolianhua.rabbitmq.springamqp.workqueue;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 17:52
 **/
@RabbitListener(queues = "task_queue")
public class Receiver {
    private final int instance;

    public Receiver(int instance) {
        this.instance = instance;
    }

    @RabbitHandler
    public void receive(String message) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println(String.format("Instance %d [x] received %s",instance,message));
        doWork(message);
        watch.stop();
        System.out.println(String.format("Instance %d [x] Done in %s s",instance,watch.getTotalTimeSeconds()));
    }

    private void doWork(String message) throws InterruptedException {
        for (char c : message.toCharArray()) {
            if (c == '.')
                TimeUnit.SECONDS.sleep(1);
        }
    }
}
