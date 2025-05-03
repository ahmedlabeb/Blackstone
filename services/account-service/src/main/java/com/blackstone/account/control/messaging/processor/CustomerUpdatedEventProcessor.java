package com.blackstone.account.control.messaging.processor;

import com.blackstone.account.common.annotation.CustomerEvent;
import com.blackstone.account.entity.messaging.consumer.events.CustomerUpdateEvent;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import com.blackstone.account.entity.domain.CustomerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@CustomerEvent("CUSTOMER_UPDATED_EVENT")
@Slf4j
public class CustomerUpdatedEventProcessor implements CustomerEventProcessor<CustomerUpdateEvent> {
    private final CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerUpdatedEventProcessor(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    @Override
    public void processCustomerEvent(CustomerUpdateEvent customerCreatedEvent) {
        CustomerInfo customerById = customerInfoRepository.findCustomerById(customerCreatedEvent.getCustomerId());
        if (customerById != null) {
            customerById.setType(customerCreatedEvent.getCustomerType());
            customerInfoRepository.save(customerById);
        }
    }
}
