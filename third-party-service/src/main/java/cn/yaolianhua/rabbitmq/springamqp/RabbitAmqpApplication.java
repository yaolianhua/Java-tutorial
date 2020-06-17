package cn.yaolianhua.rabbitmq.springamqp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 14:53
 **/
@SpringBootApplication
@EnableScheduling
public class RabbitAmqpApplication {

    @Bean
    @Profile("rabbitmq")
    public CommandLineRunner runner(){
        return new RabbitAmqpRunner();
    }

    @Bean
    @Profile("rabbitmq")
    public CommandLineRunner usage(){
        return args -> {
            System.out.println("This app uses Spring Profiles to control its behavior.\n");
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(RabbitAmqpApplication.class,args);
    }
}
