package com.maverick.rmqscheduler.util;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RMQSchedulerUtil {

    public static ResponseEntity<ApiResponseDto> getResponseEntity(String status, String errorMessage, HttpStatus httpStatus) {
        ApiResponseDto responseDto = ApiResponseDto.builder()
                .status(status)
                .errorMessage(errorMessage)
                .uuid(MDC.get(LiteralConstants.UUID))
                .build();
        log.info("RMQSchedulerUtil.getResponseEntity():: returning responseDto={}", responseDto);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
