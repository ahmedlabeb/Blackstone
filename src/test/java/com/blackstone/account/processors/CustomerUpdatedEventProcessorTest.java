package com.blackstone.account.processors;

import com.blackstone.account.control.messaging.processor.CustomerUpdatedEventProcessor;
import com.blackstone.account.entity.domain.CustomerInfo;
import com.blackstone.account.entity.messaging.consumer.events.CustomerUpdateEvent;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerUpdatedEventProcessorTest {

    private CustomerInfoRepository repo;
    private CustomerUpdatedEventProcessor processor;

    @BeforeEach
    void init() {
        repo = mock(CustomerInfoRepository.class);
        processor = new CustomerUpdatedEventProcessor(repo);
    }

    @Test
    void updatesType_whenCustomerExists() {
        CustomerInfo ci = new CustomerInfo();
        ci.setCustomerId("cust3");
        ci.setType("OLD");
        when(repo.findCustomerById("cust3")).thenReturn(ci);
        processor.processCustomerEvent(new CustomerUpdateEvent("cust3", "NEW"));
        assertThat(ci.getType()).isEqualTo("NEW");
        verify(repo).save(ci);
    }

    @Test
    void skipsSave_whenCustomerMissing() {
        when(repo.findCustomerById("cust3")).thenReturn(null);
        processor.processCustomerEvent(new CustomerUpdateEvent("cust3", "NEW"));
        verify(repo, never()).save(any());
    }
}
