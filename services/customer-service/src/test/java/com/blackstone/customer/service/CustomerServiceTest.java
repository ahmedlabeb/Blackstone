package com.blackstone.customer.service;

import com.blackstone.customer.boundry.messaging.producer.RabbitMqProducer;
import com.blackstone.customer.common.exception.CustomerServiceError;
import com.blackstone.customer.common.exception.CustomerServiceException;
import com.blackstone.customer.common.mapper.CustomerDataMapper;
import com.blackstone.customer.control.integration.AccountServiceClient;
import com.blackstone.customer.control.service.CustomerService;
import com.blackstone.customer.entity.domain.Customer;
import com.blackstone.customer.entity.dto.CustomerDto;
import com.blackstone.customer.entity.dto.CustomerType;
import com.blackstone.customer.entity.messaging.producer.events.CustomerCreatedEvent;
import com.blackstone.customer.entity.messaging.producer.events.CustomerDeleteEvent;
import com.blackstone.customer.entity.messaging.producer.events.CustomerUpdateEvent;
import com.blackstone.customer.entity.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RabbitMqProducer eventProducer;

    @Mock
    private CustomerDataMapper customerDataMapper;

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto dto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = new CustomerDto();
        dto.setCustomerId("cust-1");
        dto.setName("John Doe");
        dto.setType(CustomerType.RETAIL);

        customer = new Customer();
        customer.setCustomerId("cust-1");
        customer.setType(CustomerType.RETAIL);
        customer.setDeleted(false);
    }

    @Test
    void testCreateCustomer_success() {
        Mockito.when(customerDataMapper.toEntity(dto)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
        Mockito.when(customerDataMapper.toDTO(customer)).thenReturn(dto);

        CustomerDto result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("cust-1", result.getCustomerId());
        Mockito.verify(eventProducer).publishEvent(any(CustomerCreatedEvent.class));
    }

    @Test
    void testGetCustomer_success() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.of(customer));
        Mockito.when(customerDataMapper.toDTO(customer)).thenReturn(dto);

        CustomerDto result = customerService.getCustomer("cust-1");
        assertEquals("cust-1", result.getCustomerId());
    }

    @Test
    void testGetCustomer_notFound() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.empty());
        var ex = assertThrows(CustomerServiceException.class, () -> customerService.getCustomer("cust-1"));
        assertEquals(CustomerServiceError.CUSTOMER_NOT_EXIST, ex.getCustomerServiceError());

    }

    @Test
    void testGetAllCustomers_success() {
        Mockito.when(customerRepository.findAllActive()).thenReturn(List.of(customer));
        Mockito.when(customerDataMapper.toDtoList(List.of(customer))).thenReturn(List.of(dto));

        List<CustomerDto> customers = customerService.getAllCustomers();
        assertEquals(1, customers.size());
    }

    @Test
    void testUpdateCustomer_success() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        CustomerDto result = customerService.updateCustomer("cust-1", dto);
        assertEquals("cust-1", result.getCustomerId());
        Mockito.verify(eventProducer).publishEvent(any(CustomerUpdateEvent.class));
    }

    @Test
    void testUpdateCustomer_notFound() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.empty());
        var ex = assertThrows(CustomerServiceException.class, () -> customerService.updateCustomer("cust-1", dto));
        assertEquals(CustomerServiceError.CUSTOMER_NOT_EXIST, ex.getCustomerServiceError());

    }

    @Test
    void testDeleteCustomer_success() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.of(customer));
        Mockito.when(accountServiceClient.hasNonZeroBalance("cust-1")).thenReturn(false);
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        customerService.deleteCustomer("cust-1");
        Mockito.verify(eventProducer).publishEvent(any(CustomerDeleteEvent.class));
    }

    @Test
    void testDeleteCustomer_withActiveAccounts() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.of(customer));
        Mockito.when(accountServiceClient.hasNonZeroBalance("cust-1")).thenReturn(true);

        var ex = assertThrows(CustomerServiceException.class, () -> customerService.deleteCustomer("cust-1"));

        assertEquals(CustomerServiceError.REQUEST_VALIDATION_ERROR, ex.getCustomerServiceError());

    }

    @Test
    void testDeleteCustomer_notFound() {
        Mockito.when(customerRepository.findActiveById("cust-1")).thenReturn(Optional.empty());
        var ex = assertThrows(CustomerServiceException.class, () -> customerService.deleteCustomer("cust-1"));
        assertEquals(CustomerServiceError.CUSTOMER_NOT_EXIST, ex.getCustomerServiceError());

    }

}
