package com.blackstone.account.processors;

import com.blackstone.account.control.messaging.processor.CustomerDeletedEventProcessor;
import com.blackstone.account.entity.domain.CustomerInfo;
import com.blackstone.account.entity.messaging.consumer.events.CustomerDeleteEvent;
import com.blackstone.account.entity.repository.CustomerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CustomerDeletedEventProcessorTest {

    private CustomerInfoRepository repo;
    private CustomerDeletedEventProcessor processor;

    @BeforeEach
    void init() {
        repo = mock(CustomerInfoRepository.class);
        processor = new CustomerDeletedEventProcessor(repo);
    }

    @Test
    void deletesCustomer_whenExists() {
        CustomerInfo ci = new CustomerInfo();
        when(repo.findCustomerById("cust2")).thenReturn(ci);
        processor.processCustomerEvent(new CustomerDeleteEvent("cust2", "RETAIL"));
        verify(repo).delete(ci);
    }

    @Test
    void skipsDelete_whenMissing() {
        when(repo.findCustomerById("cust2")).thenReturn(null);
        processor.processCustomerEvent(new CustomerDeleteEvent("cust2", "RETAIL"));
        verify(repo, never()).delete(any());
    }
}
