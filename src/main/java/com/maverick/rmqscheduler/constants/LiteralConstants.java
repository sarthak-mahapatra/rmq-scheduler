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
}
