package cn.yaolianhua.rabbitmq.client.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 11:41
 **/
public class Task {
    private static final String TASK_QUEUE_NAME="task_queue";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
            String line = "";
            while (!"exit".equals(line)){
                final Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
                String message = String.join(" ",line);
                if (!"exit".equals(line)){
                    channel.basicPublish("",
                            TASK_QUEUE_NAME,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            message.getBytes(StandardCharsets.UTF_8));
                    System.out.println(String.format("[x] Sent %s",message));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
