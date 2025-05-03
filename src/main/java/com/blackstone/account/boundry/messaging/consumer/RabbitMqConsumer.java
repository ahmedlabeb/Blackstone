package com.blackstone.account.boundry.messaging.consumer;


import com.blackstone.account.common.factory.CustomerProcessorFactory;
import com.blackstone.account.entity.messaging.consumer.events.CustomerEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RabbitMqConsumer {

    private final CustomerProcessorFactory customerProcessorFactory;

    @Autowired
    public RabbitMqConsumer(CustomerProcessorFactory customerProcessorFactory) {
        this.customerProcessorFactory = customerProcessorFactory;
    }
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(@Payload CustomerEventMessage customerEventMessage, @Headers Map<String,String> eventHeaders) {
        var implementation = customerProcessorFactory.getImplementation(customerEventMessage.getCustomerBaseEvent().getType());
        implementation.processCustomerEvent(customerEventMessage.getCustomerBaseEvent());
    }
}

