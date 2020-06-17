package cn.yaolianhua.rabbitmq.springamqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 14:26
 **/
public class RabbitAmqpRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ready ... running for " + duration + " ms");
        TimeUnit.MILLISECONDS.sleep(duration);
//        ctx.close();//java.lang.IllegalStateException
    }
    @Autowired
    private ConfigurableApplicationContext ctx;

    @Value("${rabbitmq.client.duration:0}")
    private int duration;
}
