package com.blackstone.customer.processors;

import com.blackstone.customer.control.messaging.processor.AccountDeletedEventProcessor;
import com.blackstone.customer.entity.domain.AccountInfo;
import com.blackstone.customer.entity.messaging.consumer.events.AccountDeletedEvent;
import com.blackstone.customer.entity.repository.AccountInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AccountDeletedEventProcessorTest {

    private AccountInfoRepository accountInfoRepository;
    private AccountDeletedEventProcessor processor;

    @BeforeEach
    void setUp() {
        accountInfoRepository = mock(AccountInfoRepository.class);
        processor = new AccountDeletedEventProcessor(accountInfoRepository);
    }

    @Test
    void shouldDeleteAccountInfo_whenAccountExists() {
        // Given
        String accountId = "acc-789";
        AccountDeletedEvent event = new AccountDeletedEvent(accountId);
        AccountInfo mockAccountInfo = new AccountInfo();
        when(accountInfoRepository.findByAccountId(accountId)).thenReturn(mockAccountInfo);

        // When
        processor.processCustomerEvent(event);

        // Then
        verify(accountInfoRepository).findByAccountId(accountId);
        verify(accountInfoRepository).delete(mockAccountInfo);
    }

    @Test
    void shouldDoNothing_whenAccountInfoNotFound() {
        // Given
        String accountId = "acc-000";
        AccountDeletedEvent event = new AccountDeletedEvent(accountId);
        when(accountInfoRepository.findByAccountId(accountId)).thenReturn(null);

        // When
        processor.processCustomerEvent(event);

        // Then
        verify(accountInfoRepository).findByAccountId(accountId);
        verify(accountInfoRepository, never()).delete(any());
    }
}
