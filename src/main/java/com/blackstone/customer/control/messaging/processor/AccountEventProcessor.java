package com.blackstone.customer.control.messaging.processor;

import com.blackstone.customer.entity.messaging.consumer.events.AccountBaseEvent;

public interface AccountEventProcessor<T extends AccountBaseEvent> {

    public void processCustomerEvent(T t);
}
