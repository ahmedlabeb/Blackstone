package com.blackstone.account.messaging.processor;

import com.blackstone.account.annotation.CustomerEvent;
import com.blackstone.account.messaging.events.customer.CustomerCreatedEvent;
import com.blackstone.account.repository.CustomerInfoRepository;
import com.blackstone.account.repository.entity.CustomerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@CustomerEvent("CUSTOMER_CREATED_EVENT")
@Slf4j
public class CustomerCreatedEventProcessor implements CustomerEventProcessor<CustomerCreatedEvent> {
    private final CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerCreatedEventProcessor(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    @Override
    public void processCustomerEvent(CustomerCreatedEvent customerCreatedEvent) {
        CustomerInfo customerById = customerInfoRepository.findCustomerById(customerCreatedEvent.getCustomerId());
        if (customerById == null) {
            CustomerInfo customerInfo=new CustomerInfo();
            customerInfo.setCustomerId(customerCreatedEvent.getCustomerId());
            customerInfo.setType(customerCreatedEvent.getCustomerType());
            customerInfoRepository.save(customerInfo);
        }
    }
}
