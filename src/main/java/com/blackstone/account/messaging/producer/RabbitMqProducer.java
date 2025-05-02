package com.blackstone.account.messaging.producer;

import com.blackstone.account.messaging.events.account.AccountBaseEvent;
import com.blackstone.account.messaging.events.account.AccountEventMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProducer {
    private AmqpTemplate rabbitTemplate;
    public RabbitMqProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void publishEvent(AccountBaseEvent baseEvent) {
        AccountEventMessage accountEventMessage =new AccountEventMessage(baseEvent);
        rabbitTemplate.convertAndSend(baseEvent.getExchange(), baseEvent.getRoutingKey(), accountEventMessage);
    }
}
