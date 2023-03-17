package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.dto.ApiResponseDto;
import com.maverick.rmqscheduler.dto.MessageRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private RabbitService rabbitService;


    public ApiResponseDto createMessage(MessageRequestDto messageRequestDto) {

        return ApiResponseDto.builder().build();
    }

}
