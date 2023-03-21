package com.maverick.rmqscheduler.exception;

import java.io.Serializable;

public class RMQSchedulerServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 12112344232151251L;

    private String message;

    public RMQSchedulerServiceException(String message) {
        super(message);
    }
}
