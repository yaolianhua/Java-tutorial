package cn.yaolianhua.rabbitmq.client.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 15:03
 **/
public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        final Channel channel = factory.newConnection().createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        final String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println(String.format(" [*] Waiting for messages. To exit press CTRL+C, queue name %s",queue));

        DeliverCallback callback =
                (consumerTag, delivery) ->
                        System.out.println(String.format("[x] Received %s",
                                new String(delivery.getBody(), StandardCharsets.UTF_8)));

        channel.basicConsume(queue,true,callback,consumerTag -> {});
    }
}
