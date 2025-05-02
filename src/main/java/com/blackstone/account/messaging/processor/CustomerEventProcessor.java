package com.blackstone.account.messaging.processor;

import com.blackstone.account.messaging.events.customer.CustomerBaseEvent;

public interface CustomerEventProcessor<T extends CustomerBaseEvent> {

    public void processCustomerEvent(T t);
}
