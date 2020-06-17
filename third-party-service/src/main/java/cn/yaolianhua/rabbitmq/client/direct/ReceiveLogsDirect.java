package cn.yaolianhua.rabbitmq.client.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 15:49
 **/
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        final Channel channel = factory.newConnection().createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        final String queue = channel.queueDeclare().getQueue();
        final String level = level();
        channel.queueBind(queue,EXCHANGE_NAME, level);
        System.out.println(String.format(" [*] Waiting for messages. To exit press CTRL+C, " +
                "queue name %s, Routing Key %s",queue,level));

        DeliverCallback callback =
                (consumerTag, delivery) ->
                        System.out.println(String.format("[x] Received %s, Routing Key %s",
                                new String(delivery.getBody(), StandardCharsets.UTF_8)
                                ,delivery.getEnvelope().getRoutingKey()));

        channel.basicConsume(queue,true,callback,consumerTag -> {});
    }
    private static String level(){
        final String[] level = {"info", "warning", "error"};
        return level[new Random().nextInt(3)];
    }
}
