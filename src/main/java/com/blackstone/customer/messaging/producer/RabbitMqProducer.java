package com.blackstone.customer.messaging.producer;

import com.blackstone.customer.messaging.events.customer.CustomerBaseEvent;
import com.blackstone.customer.messaging.events.customer.CustomerEventMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProducer {
    private AmqpTemplate rabbitTemplate;
    public RabbitMqProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void publishEvent(CustomerBaseEvent baseEvent) {
        CustomerEventMessage customerEventMessage=new CustomerEventMessage(baseEvent);
        rabbitTemplate.convertAndSend(baseEvent.getExchange(), baseEvent.getRoutingKey(), customerEventMessage);
    }
}
