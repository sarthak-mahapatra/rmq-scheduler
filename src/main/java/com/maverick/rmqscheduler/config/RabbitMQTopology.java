package com.maverick.rmqscheduler.config;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {


    @Value("${messaging.exchange}")
    private String exchange;

    @Value("${messaging.backoff.queue.name}")
    private String backOffQueueName;

    @Value("${messaging.processing.queue.name}")
    private String processingQueueName;


    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(this.exchange).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(String.join(".", LiteralConstants.DEAD_LETTER, this.exchange)).build();
    }

    @Bean
    public Queue backoffQueue() {
        return QueueBuilder.durable(backOffQueueName)
                .withArgument(LiteralConstants.X_DEAD_LETTER_EXCHANGE, String.join(".", LiteralConstants.DEAD_LETTER, this.exchange))
                .withArgument(LiteralConstants.X_DEAD_LETTER_ROUTING_KEY,
                        String.join(".", processingQueueName, LiteralConstants.ROUTING_KEY))
                .build();
    }

    @Bean
    public Queue processingQueue() {
        return QueueBuilder.durable(processingQueueName).build();
    }

    @Bean
    public Binding backoffQueueDeadLetterBinding() {
        return BindingBuilder.bind(backoffQueue())
                .to(deadLetterExchange())
                .with(String.join(".", backOffQueueName, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding backoffQueueBinding() {
        return BindingBuilder.bind(backoffQueue())
                .to(exchange())
                .with(String.join(".", backOffQueueName, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding processingQueueDeadLetterBinding() {
        return BindingBuilder.bind(processingQueue())
                .to(deadLetterExchange())
                .with(String.join(".", processingQueueName, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding processingQueueBinding() {
        return BindingBuilder.bind(processingQueue())
                .to(exchange())
                .with(String.join(".", processingQueueName, LiteralConstants.ROUTING_KEY));
    }


}
