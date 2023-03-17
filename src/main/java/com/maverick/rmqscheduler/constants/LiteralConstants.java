package com.maverick.rmqscheduler.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LiteralConstants {

    public static final String UUID = "uuid";
    public static final String ROUTING_KEY = "routingkey";

    public static final String QUEUE_MESSAGE_COUNT = "QUEUE_MESSAGE_COUNT";

    public static final String REQUEST_ID = "requestId";

    public static final String DEAD_LETTER = "dead-letter";

    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
}
