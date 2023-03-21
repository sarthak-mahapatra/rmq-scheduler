package com.maverick.rmqscheduler.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.exception.RMQSchedulerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(value = "DemoQueueListener")
@RequiredArgsConstructor
@Slf4j
public class DemoQueueListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = LiteralConstants.DEMO_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    protected void listenMessage(Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        MDC.put(LiteralConstants.UUID, correlationId);
        log.info("ProcessingQueueListener.listenMessage() :: consumed message={} with correlationId={}", message, correlationId);
        try {
            String payload = objectMapper.readValue(message.getBody(), String.class);
            log.info("ProcessingQueueListener.listenMessage() :: payload={} with uuid={}", payload, correlationId);
        } catch (IOException e) {
            throw new RMQSchedulerServiceException(e.getMessage());
        }
    }
}
