package com.blackstone.customer.processors;

import com.blackstone.customer.control.messaging.processor.AccountCreatedEventProcessor;
import com.blackstone.customer.entity.domain.AccountInfo;
import com.blackstone.customer.entity.domain.Customer;
import com.blackstone.customer.entity.messaging.consumer.events.AccountCreatedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import com.blackstone.customer.entity.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AccountCreatedEventProcessorTest {

    private AccountInfoRepository accountInfoRepository;
    private CustomerRepository customerRepository;
    private AccountCreatedEventProcessor processor;

    @BeforeEach
    void setUp() {
        accountInfoRepository = mock(AccountInfoRepository.class);
        customerRepository = mock(CustomerRepository.class);
        processor = new AccountCreatedEventProcessor(accountInfoRepository, customerRepository);
    }

    @Test
    void shouldProcessEventAndSaveAccountInfo_whenCustomerExists() {
        AccountCreatedEvent event = new AccountCreatedEvent();
        event.setCustomerId("cust123");
        event.setAccountId("acc456");
        event.setAccountType("SAVING");

        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId("cust123");

        when(customerRepository.findActiveById("cust123")).thenReturn(Optional.of(mockCustomer));

        processor.processCustomerEvent(event);

        ArgumentCaptor<AccountInfo> captor = ArgumentCaptor.forClass(AccountInfo.class);
        verify(accountInfoRepository, times(1)).save(captor.capture());
        AccountInfo saved = captor.getValue();

        assertThat(saved.getCustomerId()).isEqualTo(mockCustomer);
        assertThat(saved.getAccountId()).isEqualTo("acc456");
        assertThat(saved.getType()).isEqualTo("ACCOUNT_CREATED_EVENT");
    }

    @Test
    void shouldNotSaveAccountInfo_whenCustomerDoesNotExist() {
        AccountCreatedEvent event = new AccountCreatedEvent();
        event.setCustomerId("cust123");
        event.setAccountId("acc456");
        event.setAccountType("SAVING");

        when(customerRepository.findActiveById("cust123")).thenReturn(Optional.empty());

        processor.processCustomerEvent(event);

        verify(accountInfoRepository, never()).save(any(AccountInfo.class));
    }
}
