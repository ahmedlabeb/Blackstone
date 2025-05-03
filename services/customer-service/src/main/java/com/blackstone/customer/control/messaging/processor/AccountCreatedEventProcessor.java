package com.blackstone.customer.control.messaging.processor;

import com.blackstone.customer.common.annotation.AccountEvent;
import com.blackstone.customer.entity.messaging.consumer.events.AccountCreatedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import com.blackstone.customer.entity.repository.CustomerRepository;
import com.blackstone.customer.entity.domain.AccountInfo;
import com.blackstone.customer.entity.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@AccountEvent("ACCOUNT_CREATED_EVENT")
@Slf4j
public class AccountCreatedEventProcessor implements AccountEventProcessor<AccountCreatedEvent> {
    private final AccountInfoRepository accountInfoRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public AccountCreatedEventProcessor(AccountInfoRepository accountInfoRepository, CustomerRepository customerRepository) {
        this.accountInfoRepository = accountInfoRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void processCustomerEvent(AccountCreatedEvent accountCreatedEvent) {
        Optional<Customer> optionalCustomer = customerRepository.findActiveById(accountCreatedEvent.getCustomerId());
        if (optionalCustomer.isPresent()) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setCustomerId(optionalCustomer.get());
            accountInfo.setAccountId(accountCreatedEvent.getAccountId());
            accountInfo.setType(accountCreatedEvent.getType());
            accountInfoRepository.save(accountInfo);
        }
    }
}
