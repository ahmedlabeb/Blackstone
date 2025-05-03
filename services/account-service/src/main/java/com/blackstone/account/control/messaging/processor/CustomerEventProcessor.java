package com.blackstone.account.control.messaging.processor;

import com.blackstone.account.entity.messaging.consumer.events.CustomerBaseEvent;

public interface CustomerEventProcessor<T extends CustomerBaseEvent> {

    public void processCustomerEvent(T t);
}
