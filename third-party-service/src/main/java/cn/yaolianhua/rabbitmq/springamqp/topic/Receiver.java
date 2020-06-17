package cn.yaolianhua.rabbitmq.springamqp.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 22:47
 **/
public class Receiver {
    public void receive(String message,int receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println(String.format("Instance %s  [x] Received %s",receiver,message));
        doWork(message);
        watch.stop();
        System.out.println(String.format("Instance %s  [x] Done in %s s",receiver,watch.getTotalTimeSeconds()));
    }

    private void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String message) throws InterruptedException {
        receive(message,1);
    }
    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String message) throws InterruptedException {
        receive(message,2);
    }
}
