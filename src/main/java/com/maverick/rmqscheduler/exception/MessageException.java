package com.maverick.rmqscheduler.exception;

import java.io.Serializable;

public class MessageException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 12112351251L;

    private String message;

    public MessageException(String message) {
        super(message);
    }
}
