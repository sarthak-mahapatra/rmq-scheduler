package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Slf4j
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.exchange}")
    private String exchange;


    public boolean pushMessageToQueue(final Object payLoad, String queueName) {
        log.info("RabbitService.pushEventToQueue :: Called with payLoad = {} and queueName = {}", payLoad, queueName);
        MessageDto message = MessageDto.builder().payload(payLoad).uuid(MDC.get(LiteralConstants.UUID)).build();
        log.info("RabbitService.pushEventToQueue :: Message={} created", message);
        String routingKey = String.join(".", LiteralConstants.ROUTING_KEY, queueName);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setCorrelationId(MDC.get(LiteralConstants.UUID));
            return messagePostProcessor;
        });
        log.info("RabbitService.pushEventToQueue :: Successfully Sent queueEvent to {}", queueName);
        return true;
    }

}
