package cn.yaolianhua.rabbitmq.client.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 21:00
 **/
public class RpcClient {
    private final Connection connection;
    private final Channel channel;
    private final String requestQueueName = "rpc_queue";
    public RpcClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] args) throws IOException, TimeoutException {

            final RpcClient rpcClient = new RpcClient();
            IntStream.range(0,32)
                    .forEach(i -> {
                        final String s = Integer.toString(i);
                        System.out.println(String.format("[x] request fib(%s)",s));
                        try {
                            final String response = rpcClient.call(s);
                            System.out.println(String.format("[.] Got %s",response));
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    });
    }


    public String call(String message) throws IOException, InterruptedException {
        final String id = UUID.randomUUID().toString();
        final String queue = channel.queueDeclare().getQueue();
        final AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(id)
                .replyTo(queue)//通常用于命名回调队列
                .build();
        channel.basicPublish("",requestQueueName,props,message.getBytes(StandardCharsets.UTF_8));
        final ArrayBlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        final String consume = channel.basicConsume(queue, true,
                (consumerTag, delivery) -> {
                    if (delivery.getProperties().getCorrelationId().equals(id)){
                        response.offer(new String(delivery.getBody(),StandardCharsets.UTF_8));
                    }
                }, consumerTag -> {
                });
        final String result = response.take();
        channel.basicCancel(consume);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}
