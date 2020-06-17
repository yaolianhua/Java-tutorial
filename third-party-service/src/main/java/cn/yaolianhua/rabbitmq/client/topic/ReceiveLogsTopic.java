package cn.yaolianhua.rabbitmq.client.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 17:04
 **/
public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        final Channel channel = factory.newConnection().createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        final String queue = channel.queueDeclare().getQueue();
        final String bindingKey = key();
        channel.queueBind(queue,EXCHANGE_NAME, bindingKey);
        System.out.println(String.format(" [*] Waiting for messages. To exit press CTRL+C, " +
                "queue name %s, Routing Key %s",queue,bindingKey));

        DeliverCallback callback =
                (consumerTag, delivery) ->
                        System.out.println(String.format("[x] Received %s, Routing Key %s",
                                new String(delivery.getBody(), StandardCharsets.UTF_8)
                                ,delivery.getEnvelope().getRoutingKey()));

        channel.basicConsume(queue,true,callback,consumerTag -> {});
    }
    private static String key(){
        final String[] keys = {"*.orange.*","*.*.rabbit","lazy.#","#"};
        return keys[new Random().nextInt(4)];
    }
}
