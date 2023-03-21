package com.maverick.rmqscheduler.listener;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.MessageDto;
import com.maverick.rmqscheduler.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component(value = "StagingQueueListener")
@RequiredArgsConstructor
@Slf4j
public class StagingQueueListener {

    private final MessageService messageService;

    @RabbitListener(queues = "${messaging.staging.queue.name}", containerFactory = "simpleRabbitListenerContainerFactory")
    protected void listenMessage(
            MessageDto messageDto, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        MDC.put(LiteralConstants.UUID, correlationId);
        log.info("ProcessingQueueListener.listenMessage() :: consumed message={} with correlationId={}", messageDto, correlationId);
        messageService.enqueueMessage(messageDto);
    }
}
