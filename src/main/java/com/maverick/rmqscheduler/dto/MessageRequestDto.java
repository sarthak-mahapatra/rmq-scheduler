package com.maverick.rmqscheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRequestDto {

    private Object payload;

    private Long delayInMs;

    private String queueName;
}
