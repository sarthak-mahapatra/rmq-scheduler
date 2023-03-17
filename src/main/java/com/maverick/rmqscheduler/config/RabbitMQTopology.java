package com.maverick.rmqscheduler.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {


    @Value("${messaging.exchange}")
    private String exchange;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(this.exchange);
    }


}
