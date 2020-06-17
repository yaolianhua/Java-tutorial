package cn.yaolianhua.rabbitmq.client.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 15:49
 **/
public class LogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");

        try (final Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String line = "";
            while (!"exit".equals(line)){
                line = new Scanner(System.in).nextLine();
                if (!"exit".equals(line)){
                    final String level = level();
                    channel.basicPublish(EXCHANGE_NAME,
                            level,null,
                            line.getBytes(StandardCharsets.UTF_8)
                    );
                    System.out.println(String.format("[x] Sent %s: %s", level,line));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static String level(){
        final String[] level = {"info", "warning", "error"};
        return level[new Random().nextInt(3)];
    }
}
