package cn.yaolianhua.rabbitmq.client.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 15:02
 **/
public class Log {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        try (final Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String line = "";
            while (!"exit".equals(line)){
                line = new Scanner(System.in).nextLine();
                if (!"exit".equals(line)){
                    channel.basicPublish(EXCHANGE_NAME,
                            "",null,
                            line.getBytes(StandardCharsets.UTF_8)
                    );
                    System.out.println(String.format("[x] Sent %s",line));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
