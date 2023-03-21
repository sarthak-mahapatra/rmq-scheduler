package com.maverick.rmqscheduler.exception;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import com.maverick.rmqscheduler.dto.ApiResponseDto;
import com.maverick.rmqscheduler.util.RMQSchedulerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RMQSchedulerExceptionHandler {

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ApiResponseDto> handleMessageException(MessageException e) {
        log.error("RMQSchedulerExceptionHandler.handleMessageException():: exception = {}", e.getMessage());
        return RMQSchedulerUtil.getResponseEntity(LiteralConstants.FAILURE, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RMQSchedulerServiceException.class)
    public ResponseEntity<ApiResponseDto> handleRMQSchedulerServiceException(RMQSchedulerServiceException e) {
        log.error("RMQSchedulerExceptionHandler.handleRMQSchedulerServiceException():: exception = {}", e.getMessage());
        return RMQSchedulerUtil.getResponseEntity(LiteralConstants.FAILURE, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}