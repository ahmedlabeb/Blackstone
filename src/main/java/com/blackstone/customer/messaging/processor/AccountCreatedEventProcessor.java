package com.blackstone.customer.messaging.processor;

import com.blackstone.customer.annotation.AccountEvent;
import com.blackstone.customer.messaging.events.account.AccountCreatedEvent;
import com.blackstone.customer.repository.AccountInfoRepository;
import com.blackstone.customer.repository.CustomerRepository;
import com.blackstone.customer.repository.entity.AccountInfo;
import com.blackstone.customer.repository.entity.Customer;
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
