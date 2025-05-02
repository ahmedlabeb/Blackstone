
package com.blackstone.account.service;

import com.blackstone.account.dto.AccountDto;
import com.blackstone.account.dto.AccountStatus;
import com.blackstone.account.dto.AccountType;
import com.blackstone.account.exception.AccountServiceError;
import com.blackstone.account.mapper.AccountDataMapper;
import com.blackstone.account.messaging.events.account.AccountCreatedEvent;
import com.blackstone.account.messaging.events.account.AccountDeletedEvent;
import com.blackstone.account.messaging.events.account.AccountUpdatedEvent;
import com.blackstone.account.messaging.producer.RabbitMqProducer;
import com.blackstone.account.repository.AccountRepository;
import com.blackstone.account.repository.CustomerInfoRepository;
import com.blackstone.account.repository.entity.Account;
import com.blackstone.account.repository.entity.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final CustomerInfoRepository customerInfoRepository;
    private final AccountDataMapper accountDataMapper;
    private final RabbitMqProducer eventProducer;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerInfoRepository customerInfoRepository, AccountDataMapper accountDataMapper, RabbitMqProducer eventProducer) {
        this.accountRepository = accountRepository;
        this.customerInfoRepository = customerInfoRepository;
        this.accountDataMapper = accountDataMapper;
        this.eventProducer = eventProducer;
    }

    public Account createAccount(AccountDto dto) {
        CustomerInfo customerInfo = customerInfoRepository.findCustomerById(dto.getCustomerId());
        validateAccount(dto, customerInfo);
        Account account = accountDataMapper.toEntity(dto);
        account.setCustomerInfo(customerInfo);
        Account savedAccount = accountRepository.save(account);
        eventProducer.publishEvent(new AccountCreatedEvent(savedAccount.getAccountId(), savedAccount.getType().name(), savedAccount.getCustomerInfo().getCustomerId()));
        return savedAccount;
    }

    public void deleteAccount(String accountId) {
        Account account = accountRepository.findActiveById(accountId)
                .orElseThrow(AccountServiceError.ACCOUNT_NOT_EXIST::buildException);
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Can't delete account that has balance, Please withdraw balance first");
        }
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        eventProducer.publishEvent(new AccountDeletedEvent(account.getAccountId()));
    }

    public AccountDto updateAccount(String accountId, AccountDto accountDto) {
        if (!accountId.equals(accountDto.getAccountId())) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Account ID in path and body must match and cannot be updated.");
        }
        Account existingAccount = accountRepository.findActiveById(accountId)
                .orElseThrow(AccountServiceError.ACCOUNT_NOT_EXIST::buildException);

        accountDataMapper.updateAccountFromDto(accountDto, existingAccount);
        accountRepository.save(existingAccount);
        eventProducer.publishEvent(new AccountUpdatedEvent(existingAccount.getAccountId(), existingAccount.getType().name(), accountDto.getCustomerId()));
        return accountDto;
    }

    public AccountDto getAccountById(String accountId) {
        Account account = accountRepository.findActiveById(accountId)
                .orElseThrow(AccountServiceError.ACCOUNT_NOT_EXIST::buildException);
        return accountDataMapper.toDTO(account);
    }

    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAllActive();
        return accountDataMapper.toDtoList(accounts);
    }

    private void validateAccount(AccountDto dto, CustomerInfo customerInfo) {
        if (customerInfo == null) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Can't create accounts for Non existing or Deactivated Customer");
        }
        if (customerInfo.getAccounts().size() >= 10) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Customer cannot have more than 10 accounts");
        }
        if (dto.getType() == AccountType.SALARY &&
                customerInfo.getAccounts().stream().anyMatch(a -> a.getType() == AccountType.SALARY)) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Only one salary account allowed per customer");
        }
        if (dto.getType() == AccountType.INVESTMENT &&
                dto.getBalance().compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Investment account must have a minimum balance of 10,000");
        }
        if (customerInfo.getType().equals("RETAIL") &&
                dto.getType() != AccountType.SAVING) {
            throw AccountServiceError.REQUEST_VALIDATION_ERROR.buildException("Retail customers can only have saving accounts");
        }
    }

    public boolean hasNonZeroBalance(String customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream().anyMatch(account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }
}
