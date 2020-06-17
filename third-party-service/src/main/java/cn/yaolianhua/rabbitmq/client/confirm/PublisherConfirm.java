package cn.yaolianhua.rabbitmq.client.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.stream.IntStream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 10:59
 **/
public class PublisherConfirm {
    private static final int MESSAGE_COUNT = 50_000;
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        publishMessageIndividually();//13356ms
//        publishMessageBatch();//2256ms
        publishConfirmsAsynchronously();//2022ms
    }
    static Connection connection() throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("app.com");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        factory.setPort(5672);
        return factory.newConnection();
    }
    static void publishMessageIndividually() throws IOException, TimeoutException {
        try (Connection connection = connection();
             final Channel channel = connection.createChannel()) {
            final String queue = UUID.randomUUID().toString();
            channel.queueDeclare(queue,false,false,true,null);
            channel.confirmSelect();
            final long t0 = Clock.systemDefaultZone().millis();
            IntStream.range(0,MESSAGE_COUNT)
                    .forEach(i -> {
                        final String body = String.valueOf(i);
                        try {
                            channel.basicPublish("",queue,null,body.getBytes(StandardCharsets.UTF_8));
                            channel.waitForConfirmsOrDie(5_000);
                        } catch (IOException | InterruptedException | TimeoutException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }

                    });
            final long t1 = Clock.systemDefaultZone().millis();
            System.out.println(String.format("Published %d messages individually in %d ms",MESSAGE_COUNT,t1 - t0));
        }
    }
    static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = connection();
             final Channel channel = connection.createChannel()) {
            final String queue = UUID.randomUUID().toString();
            channel.queueDeclare(queue,false,false,true,null);
            channel.confirmSelect();
            final int batchSize = 100;
            class C {
                int outstandingMessageCount = 0;
            }
            C c = new C();
            final long t0 = Clock.systemDefaultZone().millis();
            IntStream.range(0,MESSAGE_COUNT)
                    .forEach(i -> {
                        final String body = String.valueOf(i);
                        try {
                            channel.basicPublish("",queue,null,body.getBytes(StandardCharsets.UTF_8));
                            c.outstandingMessageCount++;
                            if (c.outstandingMessageCount == batchSize){
                                channel.waitForConfirmsOrDie(5_000);
                                c.outstandingMessageCount = 0;
                            }
                        } catch (IOException | InterruptedException | TimeoutException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }

                    });
            if (c.outstandingMessageCount > 0){
                System.out.println("outstandingMessageCount: " + c.outstandingMessageCount);
                channel.waitForConfirmsOrDie(5_000);
            }
            final long t1 = Clock.systemDefaultZone().millis();
            System.out.println(String.format("Published %d messages individually in %d ms",MESSAGE_COUNT,t1 - t0));
        }
    }
    static void publishConfirmsAsynchronously() throws IOException, TimeoutException, InterruptedException {
        try (final Connection connection = connection();
             final Channel channel = connection.createChannel()){
            final String queue = UUID.randomUUID().toString();
            channel.queueDeclare(queue,false,false,true,null);
            channel.confirmSelect();
            final ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
            ConfirmCallback cleanOutstandingConfirms = (deliveryTag, multiple) -> {
              if (multiple){
                  final ConcurrentNavigableMap<Long, String> confirmed =
                          outstandingConfirms.headMap(deliveryTag, true);
                  confirmed.clear();
              }
              else {
                  outstandingConfirms.remove(deliveryTag);
              }
            };
            channel.addConfirmListener(cleanOutstandingConfirms,(deliveryTag, multiple) -> {
                final String body = outstandingConfirms.get(deliveryTag);
                System.out.println(String.format("Message with body %s has been nack-ed. " +
                        "Sequence number: %s, multiple: %b",body,deliveryTag,multiple));
                cleanOutstandingConfirms.handle(deliveryTag,multiple);
            });
            final long t0 = Clock.systemDefaultZone().millis();
            IntStream.range(0,MESSAGE_COUNT)
                    .forEach(i -> {
                        final String body = String.valueOf(i);
                        outstandingConfirms.put(channel.getNextPublishSeqNo(),body);
                        try {
                            channel.basicPublish("",queue,null,body.getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            if (!waitUntil(Duration.ofSeconds(60), outstandingConfirms::isEmpty)){
                throw new IllegalStateException("All messages could not be confirmed in 60 seconds");
            }
            final long t1 = Clock.systemDefaultZone().millis();
            System.out.println(String.format("Publish %d Messages and handled confirms asynchronously in %d ms",MESSAGE_COUNT,t1 - t0));
        }

    }
    static boolean waitUntil(Duration timeout, BooleanSupplier supplier) throws InterruptedException {
        int waited = 0;
        while (!supplier.getAsBoolean() && waited < timeout.toMillis()){
            TimeUnit.MILLISECONDS.sleep(100L);
            waited = +100;
        }
        return supplier.getAsBoolean();
    }
}
