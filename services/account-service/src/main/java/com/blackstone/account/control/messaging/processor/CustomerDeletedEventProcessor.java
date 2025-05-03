package com.blackstone.account.control.messaging.processor;

import com.blackstone.account.common.annotation.CustomerEvent;
import com.blackstone.account.entity.messaging.consumer.events.CustomerDeleteEvent;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import com.blackstone.account.entity.domain.CustomerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@CustomerEvent("CUSTOMER_DELETED_EVENT")
@Slf4j
public class CustomerDeletedEventProcessor implements CustomerEventProcessor<CustomerDeleteEvent> {
    private final CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerDeletedEventProcessor(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    @Override
    public void processCustomerEvent(CustomerDeleteEvent customerCreatedEvent) {
        CustomerInfo customerById = customerInfoRepository.findCustomerById(customerCreatedEvent.getCustomerId());
        if (customerById != null) {
            customerInfoRepository.delete(customerById);
        }
    }
}
