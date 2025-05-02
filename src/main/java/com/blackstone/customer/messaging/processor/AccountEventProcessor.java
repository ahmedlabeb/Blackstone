package com.blackstone.customer.messaging.processor;

import com.blackstone.customer.messaging.events.account.AccountBaseEvent;

public interface AccountEventProcessor<T extends AccountBaseEvent> {

    public void processCustomerEvent(T t);
}
