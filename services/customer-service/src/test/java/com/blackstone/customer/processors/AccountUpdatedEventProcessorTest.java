package com.blackstone.customer.processors;

import com.blackstone.customer.control.messaging.processor.AccountUpdatedEventProcessor;
import com.blackstone.customer.entity.domain.AccountInfo;
import com.blackstone.customer.entity.domain.Customer;
import com.blackstone.customer.entity.messaging.consumer.events.AccountUpdatedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import com.blackstone.customer.entity.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountUpdatedEventProcessorTest {

    private AccountInfoRepository accountInfoRepository;
    private CustomerRepository customerRepository;
    private AccountUpdatedEventProcessor processor;

    @BeforeEach
    void setUp() {
        accountInfoRepository = mock(AccountInfoRepository.class);
        customerRepository = mock(CustomerRepository.class);
        processor = new AccountUpdatedEventProcessor(accountInfoRepository, customerRepository);
    }

    @Test
    void shouldSaveAccountInfo_whenCustomerExists() {
        // Arrange
        String customerId = "cust42";
        String accountId = "acc99";
        String type = "ACCOUNT_UPDATED_EVENT";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerRepository.findActiveById(customerId)).thenReturn(Optional.of(customer));
        AccountUpdatedEvent event = new AccountUpdatedEvent(accountId, type, customerId);

        // Act
        processor.processCustomerEvent(event);

        // Assert
        ArgumentCaptor<AccountInfo> captor = ArgumentCaptor.forClass(AccountInfo.class);
        verify(accountInfoRepository, times(1)).save(captor.capture());
        AccountInfo saved = captor.getValue();
        assertThat(saved.getAccountId()).isEqualTo(accountId);
        assertThat(saved.getType()).isEqualTo(type);
        assertThat(saved.getCustomerId()).isSameAs(customer);
    }

    @Test
    void shouldNotSave_whenCustomerNotFound() {
        // Arrange
        String customerId = "missing";
        when(customerRepository.findActiveById(customerId)).thenReturn(Optional.empty());
        AccountUpdatedEvent event = new AccountUpdatedEvent("acc1", "SAVING", customerId);

        // Act
        processor.processCustomerEvent(event);

        // Assert
        verify(accountInfoRepository, never()).save(any(AccountInfo.class));
    }
}
