package com.blackstone.customer.common.factory;

import com.blackstone.customer.common.annotation.AccountEvent;
import com.blackstone.customer.control.messaging.processor.AccountEventProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class AccountProcessorFactory extends AbstractAnnotationFactory<AccountEvent, AccountEventProcessor> {
    @Override
    Class<AccountEvent> strategyAnnotation() {
        return AccountEvent.class;
    }

    @Override
    Class<AccountEventProcessor> strategyInterface() {
        return AccountEventProcessor.class;
    }
}
