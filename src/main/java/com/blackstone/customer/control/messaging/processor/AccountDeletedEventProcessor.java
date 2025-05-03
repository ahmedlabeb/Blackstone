package com.blackstone.customer.control.messaging.processor;

import com.blackstone.customer.common.annotation.AccountEvent;
import com.blackstone.customer.entity.messaging.consumer.events.AccountDeletedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import com.blackstone.customer.entity.domain.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@AccountEvent("ACCOUNT_DELETED_EVENT")
@Slf4j
public class AccountDeletedEventProcessor implements AccountEventProcessor<AccountDeletedEvent> {

    private final AccountInfoRepository accountInfoRepository;

    @Autowired
    public AccountDeletedEventProcessor(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public void processCustomerEvent(AccountDeletedEvent accountDeletedEvent) {
        AccountInfo accountInfo = accountInfoRepository.findByAccountId(accountDeletedEvent.getAccountId());
        if (accountInfo != null) {
             accountInfoRepository.delete(accountInfo);
        }
    }
}
