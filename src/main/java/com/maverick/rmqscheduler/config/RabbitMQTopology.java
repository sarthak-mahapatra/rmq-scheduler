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

    @Value("${messaging.staging.queue.name}")
    private String stagingQueueName;

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
    public Queue stagingQueue() {
        return QueueBuilder.durable(stagingQueueName).build();
    }

    @Bean
    public Queue demoQueue() {
        return QueueBuilder.durable(LiteralConstants.DEMO_QUEUE).build();
    }

    @Bean
    public Queue processingQueue() {
        return QueueBuilder.durable(processingQueueName).build();
    }


    @Bean
    public Binding stagingQueueBinding() {
        return BindingBuilder.bind(stagingQueue())
                .to(exchange())
                .with(String.join(".", stagingQueueName, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding demoQueueBinding() {
        return BindingBuilder.bind(demoQueue())
                .to(exchange())
                .with(String.join(".", LiteralConstants.DEMO_QUEUE, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding processingQueueBinding() {
        return BindingBuilder.bind(processingQueue())
                .to(exchange())
                .with(String.join(".", processingQueueName, LiteralConstants.ROUTING_KEY));
    }

    @Bean
    public Binding processingQueueDeadLetterBinding() {
        return BindingBuilder.bind(processingQueue())
                .to(deadLetterExchange())
                .with(String.join(".", processingQueueName, LiteralConstants.ROUTING_KEY));
    }


}
