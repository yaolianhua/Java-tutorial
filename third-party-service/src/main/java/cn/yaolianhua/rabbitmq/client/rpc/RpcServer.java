package cn.yaolianhua.rabbitmq.client.rpc;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-14 20:59
 **/
public class RpcServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setPort(5672);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");

        try (final Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);
            channel.queuePurge(RPC_QUEUE_NAME);
            channel.basicQos(1);
            System.out.println("[x] Awaiting RPC requests");
            Object monitor = new Object();
            DeliverCallback callback = (consumerTag, delivery) -> {
                final AMQP.BasicProperties replyProps =
                        new AMQP.BasicProperties()
                        .builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                String response = "";
                try {
                    final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    final int i = Integer.parseInt(message);
                    System.out.println(String.format("[.] fib(%s)",message));
                    response += fib(i);
                }finally {
                    channel.basicPublish("",
                            delivery.getProperties().getReplyTo(),
                            replyProps,
                            response.getBytes(StandardCharsets.UTF_8)
                    );
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    synchronized (monitor){
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(RPC_QUEUE_NAME,false,callback,consumerTag -> {});
            while (true){
                synchronized (monitor){
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int fib(int n){
        if (n == 0) return 0;
        return n == 1 ? 1 : fib(n - 1) + fib(n - 2);
    }
}
