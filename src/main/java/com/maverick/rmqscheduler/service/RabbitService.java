package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.exchange}")
    private String exchange;


    public boolean pushMessageToQueue(final Object payLoad, String queueName) {
        log.info("RabbitService.pushEventToQueue :: Called with payLoad = {} and queueName = {}", payLoad, queueName);
        MessageDto message = MessageDto.builder().payload(payLoad).uuid(MDC.get(LiteralConstants.UUID)).build();
        log.info("RabbitService.pushEventToQueue :: Message={} created", message);
        String routingKey = String.join(".", queueName, LiteralConstants.ROUTING_KEY);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setCorrelationId(MDC.get(LiteralConstants.UUID));
            return messagePostProcessor;
        });
        log.info("RabbitService.pushEventToQueue :: Successfully Sent queueEvent to {}", queueName);
        return true;
    }

    public boolean pushMessageToQueueWithExpiry(final Object payLoad, String queueName, Long expiryInMs) {
        log.info("RabbitService.pushEventToQueue :: Called with payLoad = {} and queueName = {}, expiryInMs = {}",
                payLoad, queueName, expiryInMs);
        MessageDto message = MessageDto.builder().payload(payLoad).uuid(MDC.get(LiteralConstants.UUID)).delayInMs(expiryInMs).build();
        log.info("RabbitService.pushEventToQueue :: Message={} created", message);
        String routingKey = String.join(".", queueName, LiteralConstants.ROUTING_KEY);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setCorrelationId(MDC.get(LiteralConstants.UUID));
            messagePostProcessor.getMessageProperties().setExpiration(String.valueOf(expiryInMs));
            return messagePostProcessor;
        });
        log.info("RabbitService.pushEventToQueue :: Successfully Sent queueEvent to {} with expiryInMs={}",
                queueName, expiryInMs);
        return true;
    }

}
