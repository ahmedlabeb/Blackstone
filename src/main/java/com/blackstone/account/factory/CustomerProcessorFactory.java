package com.blackstone.account.factory;

import com.blackstone.account.annotation.CustomerEvent;
import com.blackstone.account.messaging.processor.CustomerEventProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class CustomerProcessorFactory extends AbstractAnnotationFactory<CustomerEvent, CustomerEventProcessor> {
    @Override
    Class<CustomerEvent> strategyAnnotation() {
        return CustomerEvent.class;
    }

    @Override
    Class<CustomerEventProcessor> strategyInterface() {
        return CustomerEventProcessor.class;
    }
}
