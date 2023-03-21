package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.ApiResponseDto;
import com.maverick.rmqscheduler.dto.MessageDto;
import com.maverick.rmqscheduler.dto.MessageRequestDto;
import com.maverick.rmqscheduler.exception.ExceptionConstants;
import com.maverick.rmqscheduler.exception.MessageException;
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

    private final RabbitAdminService rabbitAdminService;


    @Value("${messaging.staging.queue.name}")
    private String stagingQueueName;

    public ApiResponseDto createMessage(MessageRequestDto messageRequestDto) {
        log.info("MessageService.createMessage() :: received request with messageRequestDto={}", messageRequestDto);
        String queueName = messageRequestDto.getQueueName();
        Boolean isValid = rabbitAdminService.isValidQueue(queueName);

        if (Boolean.FALSE.equals(isValid)) {
            throw new MessageException(ExceptionConstants.INVALID_QUEUE_NAME);
        }

        rabbitService.pushMessageToQueue(prepareMessageForStaging(messageRequestDto), stagingQueueName);
        return ApiResponseDto.builder()
                .uuid(MDC.get(LiteralConstants.UUID))
                .status(LiteralConstants.SUCCESS)
                .build();
    }

    public void enqueueMessage(MessageDto message) {
        log.info("MessageService.enqueueMessage() :: processing message={}", message);
        String delayInString = String.join(LiteralConstants.UNDERSCORE, String.valueOf(message.getDelayInMs()), LiteralConstants.DELAY);

        String backoffQueueName = String.join(LiteralConstants.QUEUE_SEPARATOR, delayInString, LiteralConstants.BACKOFF_QUEUE_POSTFIX);
        Boolean isValid = rabbitAdminService.isValidQueue(backoffQueueName);

        if (Boolean.FALSE.equals(isValid)) {
            rabbitAdminService.createRabbitTopologyWithDelay(backoffQueueName);
        }
        rabbitService.pushMessageToQueueWithExpiry(message, backoffQueueName, message.getDelayInMs());
    }


    private MessageDto prepareMessageForStaging(MessageRequestDto messageRequestDto) {
        return MessageDto.builder().queueName(messageRequestDto.getQueueName())
                .delayInMs(messageRequestDto.getDelayInMs())
                .payload(messageRequestDto.getPayload()).uuid(MDC.get(LiteralConstants.UUID)).build();
    }

    public void processMessage(MessageDto messageDto) {
        log.info("MessageService.processMessage() :: processing message={}", messageDto);
        Object payload = messageDto.getPayload();
        String queueName = messageDto.getQueueName();
        rabbitService.pushMessageToQueue(payload, queueName);
    }

    public void deleteBackoffQueuesIfEmpty(MessageDto messageDto) {
        String delayInString = String.valueOf(messageDto.getDelayInMs());
        String backoffQueuePrefix = String.join(LiteralConstants.UNDERSCORE, delayInString, LiteralConstants.DELAY);
        String backoffQueueName = String.join(LiteralConstants.QUEUE_SEPARATOR, backoffQueuePrefix, LiteralConstants.BACKOFF_QUEUE_POSTFIX);
        rabbitAdminService.deleteBackoffQueuesIfUnused(backoffQueueName);
    }
}
