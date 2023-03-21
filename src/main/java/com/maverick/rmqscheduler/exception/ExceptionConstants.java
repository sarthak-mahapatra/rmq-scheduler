package com.maverick.rmqscheduler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionConstants {

    public static final String INVALID_QUEUE_NAME = "Queue name is invalid";
}