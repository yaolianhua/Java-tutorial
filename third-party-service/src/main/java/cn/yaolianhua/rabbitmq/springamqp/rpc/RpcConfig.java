package cn.yaolianhua.rabbitmq.springamqp.rpc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 23:10
 **/
@Configuration
@Profile("rpc")
public class RpcConfig {
    @Profile("client")
    static class ClientConfig {
        @Bean
        public Client client() {
            return new Client();
        }

    }
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("direct_logs",false,false,null);
    }
    @Profile("server")
    static class ServerConfig {

        @Bean
        public Queue queue() {
            return new Queue("rpc_queue",false,false,false,null);
        }

        @Bean
        public Binding binding(DirectExchange exchange, Queue queue) {
            return BindingBuilder.bind(queue).to(exchange).with("rpc");
        }

        @Bean
        public Server server() {
            return new Server();
        }

    }
}
