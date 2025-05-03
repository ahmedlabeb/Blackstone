package com.blackstone.account.service;

import com.blackstone.account.boundry.messaging.producer.RabbitMqProducer;
import com.blackstone.account.common.exception.AccountServiceError;
import com.blackstone.account.common.exception.AccountServiceException;
import com.blackstone.account.common.mapper.AccountDataMapper;
import com.blackstone.account.control.service.AccountService;
import com.blackstone.account.entity.domain.Account;
import com.blackstone.account.entity.domain.CustomerInfo;
import com.blackstone.account.entity.dto.AccountDto;
import com.blackstone.account.entity.dto.AccountStatus;
import com.blackstone.account.entity.dto.AccountType;
import com.blackstone.account.entity.messaging.producer.events.AccountCreatedEvent;
import com.blackstone.account.entity.messaging.producer.events.AccountDeletedEvent;
import com.blackstone.account.entity.messaging.producer.events.AccountUpdatedEvent;
import com.blackstone.account.entity.repository.AccountRepository;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerInfoRepository customerInfoRepository;

    @Mock
    private AccountDataMapper accountDataMapper;

    @Mock
    private RabbitMqProducer eventProducer;

    @InjectMocks
    private AccountService accountService;

    private AccountDto dto;
    private Account account;
    private Account account2;
    private CustomerInfo customerInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = new AccountDto();
        dto.setAccountId("acc-1");
        dto.setCustomerId("cust-1");
        dto.setBalance(BigDecimal.valueOf(15000));
        dto.setType(AccountType.SAVING);
        dto.setStatus(AccountStatus.ACTIVE);
        account = new Account();
        account.setAccountId("acc-1");
        account.setType(AccountType.SAVING);
        account.setBalance(BigDecimal.valueOf(15000));
        account.setStatus(AccountStatus.ACTIVE);

        account2 = new Account();
        account2.setAccountId("acc-2");
        account2.setBalance(BigDecimal.valueOf(0));
        account2.setStatus(AccountStatus.ACTIVE);

        customerInfo = new CustomerInfo();
        customerInfo.setCustomerId("cust-1");
        customerInfo.setAccounts(Collections.emptyList());
        customerInfo.setType("RETAIL");
    }

    @Test
    void testCreateAccount_success() {
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(customerInfo);
        Mockito.when(accountDataMapper.toEntity(dto)).thenReturn(account);
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccount(dto);
        assertNotNull(result);
        Mockito.verify(eventProducer).publishEvent(any(AccountCreatedEvent.class));
    }

    @Test
    void testCreateAccount_invalidCustomer() {
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(null);
        var ex = assertThrows(AccountServiceException.class, () -> accountService.createAccount(dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testCreateAccount_moreThan10Accounts() {
        customerInfo.setAccounts(Collections.nCopies(10, new Account()));
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(customerInfo);
        var ex = assertThrows(AccountServiceException.class, () -> accountService.createAccount(dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testCreateAccount_salaryAccountExists() {
        Account existing = new Account();
        existing.setType(AccountType.SALARY);
        customerInfo.setAccounts(List.of(existing));
        dto.setType(AccountType.SALARY);
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(customerInfo);
        var ex = assertThrows(AccountServiceException.class, () -> accountService.createAccount(dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testCreateAccount_investmentLowBalance() {
        dto.setType(AccountType.INVESTMENT);
        dto.setBalance(BigDecimal.valueOf(5000));
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(customerInfo);
        var ex = assertThrows(AccountServiceException.class, () -> accountService.createAccount(dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testCreateAccount_retailInvalidType() {
        dto.setType(AccountType.SALARY);
        Mockito.when(customerInfoRepository.findCustomerById("cust-1")).thenReturn(customerInfo);
        var ex = assertThrows(AccountServiceException.class, () -> accountService.createAccount(dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testDeleteAccount_success() {
        Mockito.when(accountRepository.findActiveById("acc-2")).thenReturn(Optional.of(account2));
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        assertDoesNotThrow(() -> accountService.deleteAccount("acc-2"));
        Mockito.verify(eventProducer).publishEvent(any(AccountDeletedEvent.class));
    }

    @Test
    void testDeleteAccount_withBalance() {
        account.setBalance(BigDecimal.valueOf(10));
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.of(account));

        var ex = assertThrows(AccountServiceException.class, () -> accountService.deleteAccount("acc-1"));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testDeleteAccount_notFound() {
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.empty());
        var ex = assertThrows(AccountServiceException.class, () -> accountService.deleteAccount("acc-1"));
        assertEquals(AccountServiceError.ACCOUNT_NOT_EXIST, ex.getAccountServiceError());
    }

    @Test
    void testUpdateAccount_success() {
        dto.setAccountId("acc-1");
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        AccountDto result = accountService.updateAccount("acc-1", dto);
        assertEquals("acc-1", result.getAccountId());
        Mockito.verify(eventProducer).publishEvent(any(AccountUpdatedEvent.class));
    }

    @Test
    void testUpdateAccount_mismatchId() {
        dto.setAccountId("acc-2");
        var ex = assertThrows(AccountServiceException.class, () -> accountService.updateAccount("acc-1", dto));
        assertEquals(AccountServiceError.REQUEST_VALIDATION_ERROR, ex.getAccountServiceError());
    }

    @Test
    void testUpdateAccount_notFound() {
        dto.setAccountId("acc-1");
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.empty());
        var ex = assertThrows(AccountServiceException.class, () -> accountService.updateAccount("acc-1", dto));
        assertEquals(AccountServiceError.ACCOUNT_NOT_EXIST, ex.getAccountServiceError());
    }

    @Test
    void testGetAccountById_success() {
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.of(account));
        Mockito.when(accountDataMapper.toDTO(account)).thenReturn(dto);

        AccountDto result = accountService.getAccountById("acc-1");
        assertEquals("acc-1", result.getAccountId());
    }

    @Test
    void testGetAccountById_notFound() {
        Mockito.when(accountRepository.findActiveById("acc-1")).thenReturn(Optional.empty());
        var ex = assertThrows(AccountServiceException.class, () -> accountService.getAccountById("acc-1"));
        assertEquals(AccountServiceError.ACCOUNT_NOT_EXIST, ex.getAccountServiceError());
    }

    @Test
    void testGetAllAccounts() {
        Mockito.when(accountRepository.findAllActive()).thenReturn(List.of(account));
        Mockito.when(accountDataMapper.toDtoList(List.of(account))).thenReturn(List.of(dto));

        List<AccountDto> result = accountService.getAllAccounts();
        assertEquals(1, result.size());
    }

    @Test
    void testHasNonZeroBalance_true() {
        account.setBalance(BigDecimal.TEN);
        Mockito.when(accountRepository.findByCustomerId("cust-1")).thenReturn(List.of(account));

        assertTrue(accountService.hasNonZeroBalance("cust-1"));
    }

    @Test
    void testHasNonZeroBalance_false() {
        account.setBalance(BigDecimal.ZERO);
        Mockito.when(accountRepository.findByCustomerId("cust-1")).thenReturn(List.of(account));

        assertFalse(accountService.hasNonZeroBalance("cust-1"));
    }
} 
