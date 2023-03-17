package com.maverick.rmqscheduler.controller;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.ApiResponseDto;
import com.maverick.rmqscheduler.dto.MessageRequestDto;
import com.maverick.rmqscheduler.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class MessageController {

    private MessageService messageService;


    @PostMapping("/message")
    public ResponseEntity<ApiResponseDto> createMessage(
            @RequestHeader(value = LiteralConstants.REQUEST_ID, required = false) String requestId,
            @RequestBody MessageRequestDto messageRequestDto
    ) {
        String uuid = Objects.nonNull(requestId) ? requestId : UUID.randomUUID().toString();
        MDC.put(LiteralConstants.UUID, uuid);
        log.info("MessageController.createMessage :: received message with uuid={}, messageRequestDto={}", uuid, messageRequestDto);
        ApiResponseDto result = messageService.createMessage(messageRequestDto);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
}
