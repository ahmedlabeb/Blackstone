package com.blackstone.customer.messaging.consumer;


import com.blackstone.customer.factory.AccountProcessorFactory;
import com.blackstone.customer.messaging.events.account.AccountEventMessage;
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
    private final AccountProcessorFactory accountProcessorFactory;

    @Autowired
    public RabbitMqConsumer(AccountProcessorFactory accountProcessorFactory) {
        this.accountProcessorFactory = accountProcessorFactory;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(@Payload AccountEventMessage accountEventMessage, @Headers Map<String,String> eventHeaders) {
        var implementation = accountProcessorFactory.getImplementation(accountEventMessage.getAccountBaseEvent().getType());
        implementation.processCustomerEvent(accountEventMessage.getAccountBaseEvent());
    }
}

