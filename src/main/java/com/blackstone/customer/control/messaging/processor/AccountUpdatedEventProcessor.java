package com.blackstone.customer.control.messaging.processor;

import com.blackstone.customer.common.annotation.AccountEvent;
import com.blackstone.customer.entity.messaging.consumer.events.AccountUpdatedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import com.blackstone.customer.entity.repository.CustomerRepository;
import com.blackstone.customer.entity.domain.AccountInfo;
import com.blackstone.customer.entity.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@AccountEvent("ACCOUNT_UPDATED_EVENT")
@Slf4j
public class AccountUpdatedEventProcessor implements AccountEventProcessor<AccountUpdatedEvent> {

    private final AccountInfoRepository accountInfoRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public AccountUpdatedEventProcessor(AccountInfoRepository accountInfoRepository, CustomerRepository customerRepository) {
        this.accountInfoRepository = accountInfoRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void processCustomerEvent(AccountUpdatedEvent accountUpdatedEvent) {
        Optional<Customer> optionalCustomer = customerRepository.findActiveById(accountUpdatedEvent.getCustomerId());
        if(optionalCustomer.isPresent()) {
            AccountInfo accountInfo=new AccountInfo();
            accountInfo.setCustomerId(optionalCustomer.get());
            accountInfo.setAccountId(accountUpdatedEvent.getAccountId());
            accountInfo.setType(accountUpdatedEvent.getType());
            accountInfoRepository.save(accountInfo);
        }
    }
}
