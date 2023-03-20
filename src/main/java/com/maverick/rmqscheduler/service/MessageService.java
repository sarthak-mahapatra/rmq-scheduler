package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.ApiResponseDto;
import com.maverick.rmqscheduler.dto.MessageDto;
import com.maverick.rmqscheduler.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final RabbitService rabbitService;

    @Value("${messaging.backoff.queue.name}")
    private String backoffQueueName;

    public ApiResponseDto createMessage(MessageRequestDto messageRequestDto) {
        log.info("MessageService.createMessage() :: received request with messageRequestDto={}", messageRequestDto);
        Boolean isMessagePushed = rabbitService.pushMessageToQueueWithExpiry(messageRequestDto.getMessage(), backoffQueueName,
                messageRequestDto.getDelayInMs());


        return ApiResponseDto.builder()
                .delay(messageRequestDto.getDelayInMs())
                .uuid(MDC.get(LiteralConstants.UUID))
                .status(Boolean.TRUE.equals(isMessagePushed) ? LiteralConstants.SUCCESS : LiteralConstants.FAILURE)
                .build();
    }

    public void processMessage(MessageDto message, String delay, String correlationId) {
        log.info("MessageService.processMessage() :: processing message={}", message);
        String receivedMessage = message.getPayload().toString();
        log.info("MessageService.processMessage() :: uuidFromProperties={}, uuidFromMessage={}", correlationId, message.getUuid());
        log.info("MessageService.processMessage() :: processed message={} with delay={}", receivedMessage, message.getDelayInMs());
    }
}
