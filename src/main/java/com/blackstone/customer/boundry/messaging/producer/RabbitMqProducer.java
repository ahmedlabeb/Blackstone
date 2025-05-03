package com.blackstone.customer.boundry.messaging.producer;

import com.blackstone.customer.entity.messaging.producer.events.CustomerBaseEvent;
import com.blackstone.customer.entity.messaging.producer.events.CustomerEventMessage;
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
