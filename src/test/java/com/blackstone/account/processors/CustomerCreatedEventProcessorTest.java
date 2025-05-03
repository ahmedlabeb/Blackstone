package com.blackstone.account.processors;

import com.blackstone.account.control.messaging.processor.CustomerCreatedEventProcessor;
import com.blackstone.account.entity.domain.CustomerInfo;
import com.blackstone.account.entity.messaging.consumer.events.CustomerCreatedEvent;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerCreatedEventProcessorTest {

    private CustomerInfoRepository repo;
    private CustomerCreatedEventProcessor processor;

    @BeforeEach
    void init() {
        repo = mock(CustomerInfoRepository.class);
        processor = new CustomerCreatedEventProcessor(repo);
    }

    @Test
    void savesNewCustomer_whenNotExisting() {
        when(repo.findCustomerById("cust1")).thenReturn(null);
        processor.processCustomerEvent(new CustomerCreatedEvent("cust1", "RETAIL"));
        ArgumentCaptor<CustomerInfo> captor = ArgumentCaptor.forClass(CustomerInfo.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getCustomerId()).isEqualTo("cust1");
        assertThat(captor.getValue().getType()).isEqualTo("RETAIL");
    }

    @Test
    void skipsSave_whenCustomerExists() {
        when(repo.findCustomerById("cust1")).thenReturn(new CustomerInfo());
        processor.processCustomerEvent(new CustomerCreatedEvent("cust1", "RETAIL"));
        verify(repo, never()).save(any());
    }
}
