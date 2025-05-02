package com.blackstone.customer.messaging.processor;

import com.blackstone.customer.annotation.AccountEvent;
import com.blackstone.customer.messaging.events.account.AccountDeletedEvent;
import com.blackstone.customer.repository.AccountInfoRepository;
import com.blackstone.customer.repository.entity.AccountInfo;
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
