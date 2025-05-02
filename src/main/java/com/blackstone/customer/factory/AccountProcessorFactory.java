package com.blackstone.customer.factory;

import com.blackstone.customer.annotation.AccountEvent;
import com.blackstone.customer.messaging.processor.AccountEventProcessor;
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
