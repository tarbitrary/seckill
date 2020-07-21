package net.xicp.tarbitrary.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String topicExchangeName = "spring-boot-exchange";

    public static final String queueName = "spring-boot";
    public static final String SEC_KILL_QUEUE_NAME = "sec-kill-queue-name";

    @Bean
    Queue topicQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue secKillQueue() {
        return new Queue(SEC_KILL_QUEUE_NAME, true);
    }

    @Bean
    TopicExchange topicExchange() {
        TopicExchange exchange = new TopicExchange(topicExchangeName);
        return exchange;
    }

    @Bean
    Binding binding(TopicExchange exchange) {
        return BindingBuilder.bind(topicQueue()).to(exchange).with("tarbitrary.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }


}
