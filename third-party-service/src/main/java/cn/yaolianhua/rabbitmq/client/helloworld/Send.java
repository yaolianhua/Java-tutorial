package cn.yaolianhua.rabbitmq.client.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-13 21:41
 **/
public class Send {
    private static final String QUEUE_NAME="hello";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "Hello World";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println(String.format("[x] Sent %s",message));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
