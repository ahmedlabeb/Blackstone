package com.blackstone.account.common.factory;

import com.blackstone.account.common.annotation.CustomerEvent;
import com.blackstone.account.control.messaging.processor.CustomerEventProcessor;
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
