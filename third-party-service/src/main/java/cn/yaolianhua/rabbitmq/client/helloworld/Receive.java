package cn.yaolianhua.rabbitmq.client.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-13 22:33
 **/
public class Receive {
    private static final String QUEUE_NAME="hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        final Channel channel = factory.newConnection().createChannel();//不关闭，保持连接状态
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println("[*] Waiting for messages, To exit press Ctrl+C");
        DeliverCallback callback = (consumerTag,delivery) -> {
            final String message = new String(delivery.getBody(), Charset.defaultCharset());
            System.out.println(String.format("[x] Received %s",message));
        };
        channel.basicConsume(QUEUE_NAME,true,callback,consumerTag -> {});
    }
}
