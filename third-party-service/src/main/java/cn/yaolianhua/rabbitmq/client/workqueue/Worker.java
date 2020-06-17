package cn.yaolianhua.rabbitmq.client.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 11:41
 **/
public class Worker {

    private static final String TASK_QUEUE_NAME="task_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        final Channel channel = factory.newConnection().createChannel();//不关闭，保持连接状态
        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        System.out.println("[*] Waiting for messages, To exit press Ctrl+C");
        channel.basicQos(1);//一次仅接受一条未经确认的消息
        DeliverCallback callback = (consumerTag, delivery) -> {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(String.format("[x] Received %s",message));
            try {
                doWork(message);
            } finally {
                System.out.println("[x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME,false,callback,consumerTag -> {});
    }

    private static void doWork(String msg){
        for (char c : msg.toCharArray()) {
            if (c == '.') {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
