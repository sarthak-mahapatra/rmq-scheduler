package com.maverick.rmqscheduler.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

@Slf4j
@NoArgsConstructor
public class FatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
    @Override
    public boolean isFatal(Throwable t) {
        if (t instanceof ListenerExecutionFailedException) {
            ListenerExecutionFailedException ex = (ListenerExecutionFailedException) t;
            log.error("Failed to process inbound message from queue " + ex.getFailedMessage().getMessageProperties().getConsumerQueue()
                    + "; failed message: " + ex.getFailedMessage(), t);
        }

        log.error("FatalExceptionStrategy.isFatal :: ex={}", t.getMessage());
        return super.isFatal(t);
    }
}
