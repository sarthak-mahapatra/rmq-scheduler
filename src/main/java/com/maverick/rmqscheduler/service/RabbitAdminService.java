package com.maverick.rmqscheduler.service;

import com.maverick.rmqscheduler.constants.LiteralConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class RabbitAdminService {
    private final RabbitAdmin rabbitAdmin;

    @Value("${messaging.exchange}")
    private String exchange;

    @Value("${messaging.processing.queue.name}")
    private String processingQueueName;

    public Boolean isValidQueue(String queueName) {
        if (Objects.isNull(queueName)) {
            return Boolean.FALSE;
        }
        QueueInformation queueInformation = rabbitAdmin.getQueueInfo(queueName);
        return queueInformation == null ? Boolean.FALSE : Boolean.TRUE;

    }

    public void createRabbitTopologyWithDelay(String backoffQueueName) {
        createBackoffQueue(backoffQueueName);
        createBackoffBindings(backoffQueueName);
    }

    private void createBackoffQueue(String backoffQueueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(LiteralConstants.X_DEAD_LETTER_EXCHANGE, String.join(LiteralConstants.QUEUE_SEPARATOR, LiteralConstants.DEAD_LETTER, this.exchange));
        arguments.put(LiteralConstants.X_DEAD_LETTER_ROUTING_KEY, String.join(LiteralConstants.QUEUE_SEPARATOR, processingQueueName, LiteralConstants.ROUTING_KEY));
        Queue queue = new Queue(backoffQueueName, true, false, false, arguments);
        rabbitAdmin.declareQueue(queue);
    }

    private void createBackoffBindings(String backoffQueueName) {
        Queue queue = new Queue(backoffQueueName);
        DirectExchange deadLetterExchange = new DirectExchange(String.join(LiteralConstants.QUEUE_SEPARATOR, LiteralConstants.DEAD_LETTER, this.exchange));
        String routingKey = String.join(LiteralConstants.QUEUE_SEPARATOR, backoffQueueName, LiteralConstants.ROUTING_KEY);
        Binding backoffQueueDeadLetterBinding = BindingBuilder.bind(queue).to(deadLetterExchange).with(routingKey);
        rabbitAdmin.declareBinding(backoffQueueDeadLetterBinding);

        DirectExchange directExchange = new DirectExchange(exchange);
        Binding backoffQueueBinding = BindingBuilder.bind(queue).to(directExchange).with(routingKey);
        rabbitAdmin.declareBinding(backoffQueueBinding);

    }

    public void deleteBackoffQueuesIfUnused(String backoffQueueName) {
        QueueInformation queueInformation = rabbitAdmin.getQueueInfo(backoffQueueName);
        if (Objects.isNull(queueInformation)) {
            return;
        }
        if (queueInformation.getMessageCount() == 0) {
            rabbitAdmin.deleteQueue(backoffQueueName, true, true);
            log.info("RabbitAdminService.deleteBackoffQueuesIfUnused() :: deleted unused queue={}", backoffQueueName);
            return;
        }
        log.info("RabbitAdminService.deleteBackoffQueuesIfUnused() :: queueName={} not deleted due to messageCount={}",
                backoffQueueName, queueInformation.getMessageCount());
    }

}
