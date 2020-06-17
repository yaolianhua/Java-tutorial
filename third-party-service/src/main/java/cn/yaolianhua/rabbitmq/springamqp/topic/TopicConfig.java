package cn.yaolianhua.rabbitmq.springamqp.topic;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-06-15 22:47
 **/
@Configuration
@Profile("topic")
public class TopicConfig {
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("topic_logs",false,false,null);
    }

    @Profile("receiver")
    static class ReceiverConfig {

        @Bean
        public Receiver receiver() {
            return new Receiver();
        }

        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding bindingA1(TopicExchange topic, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.orange.*");
        }

        @Bean
        public Binding bindingB1(TopicExchange topic, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.*.rabbit");
        }

        @Bean
        public Binding bindingA2(TopicExchange topic, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("lazy.#");
        }

    }

    @Profile("send")
    @Bean
    public Send sender() {
        return new Send();
    }
}
