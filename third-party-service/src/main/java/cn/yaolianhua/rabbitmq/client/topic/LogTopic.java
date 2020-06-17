package cn.yaolianhua.rabbitmq.client.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 17:04
 **/
public class LogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");

        try (final Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String line = "";
            while (!"exit".equals(line)){
                line = new Scanner(System.in).nextLine();
                if (!"exit".equals(line)){
                    final String level = key();
                    channel.basicPublish(EXCHANGE_NAME,
                            level,
                            null,
                            line.getBytes(StandardCharsets.UTF_8)
                    );
                    System.out.println(String.format("[x] Sent %s: %s", level,line));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static String key(){
        final String[] keys = {
                "quit.orange.rabbit",
                "lazy.orange.elephant",
                "quick.orange.fox",
                "lazy.brown.fox",
                "lazy.pink.rabbit",
                "quick.brown.fox"
        };
        return keys[new Random().nextInt(6)];
    }
}
