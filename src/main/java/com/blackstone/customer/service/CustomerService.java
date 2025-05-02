
package com.blackstone.customer.service;

import com.blackstone.customer.dto.CustomerDto;
import com.blackstone.customer.integration.AccountServiceClient;
import com.blackstone.customer.repository.entity.Customer;
import com.blackstone.customer.messaging.events.customer.CustomerCreatedEvent;
import com.blackstone.customer.messaging.events.customer.CustomerDeleteEvent;
import com.blackstone.customer.messaging.events.customer.CustomerUpdateEvent;
import com.blackstone.customer.exception.CustomerServiceError;
import com.blackstone.customer.mapper.CustomerDataMapper;
import com.blackstone.customer.messaging.producer.RabbitMqProducer;
import com.blackstone.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RabbitMqProducer eventProducer;
    private final CustomerDataMapper customerDataMapper;

    private final AccountServiceClient accountServiceClient;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, RabbitMqProducer eventProducer, CustomerDataMapper customerDataMapper, AccountServiceClient accountServiceClient) {
        this.customerRepository = customerRepository;
        this.eventProducer = eventProducer;
        this.customerDataMapper = customerDataMapper;
        this.accountServiceClient = accountServiceClient;
    }


    public CustomerDto createCustomer(CustomerDto dto) {
        Customer customer = customerDataMapper.toEntity(dto);
        Customer savedCustomer = customerRepository.save(customer);
        eventProducer.publishEvent(new CustomerCreatedEvent(savedCustomer.getCustomerId(), savedCustomer.getType().name()));
        return customerDataMapper.toDTO(savedCustomer);
    }

    public CustomerDto getCustomer(String customerId) {
        Customer customer = customerRepository.findActiveById(customerId)
                .orElseThrow(CustomerServiceError.CUSTOMER_NOT_EXIST::buildException);
        return customerDataMapper.toDTO(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAllActive();
        return customerDataMapper.toDtoList(customers);
    }

    public CustomerDto updateCustomer(String customerId, CustomerDto customerDto) {

        Customer existingCustomer = customerRepository.findActiveById(customerId)
                .orElseThrow(CustomerServiceError.CUSTOMER_NOT_EXIST::buildException);

        customerDataMapper.updateCustomerFromDto(customerDto, existingCustomer);
        customerRepository.save(existingCustomer);
        eventProducer.publishEvent(new CustomerUpdateEvent(existingCustomer.getCustomerId(), existingCustomer.getType().name()));
        return customerDto;
    }

    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findActiveById(customerId)
                .orElseThrow(CustomerServiceError.CUSTOMER_NOT_EXIST::buildException);
        validateCustomerAccounts(customerId);
        customer.setDeleted(true);
        Customer saveCustomer = customerRepository.save(customer);
        eventProducer.publishEvent(new CustomerDeleteEvent(saveCustomer.getCustomerId(), saveCustomer.getType().name()));

    }

    private void validateCustomerAccounts(String customerId) {
        Boolean hasNonZeroBalance = accountServiceClient.hasNonZeroBalance(customerId);
        if (Boolean.TRUE.equals(hasNonZeroBalance)) {
            throw CustomerServiceError.REQUEST_VALIDATION_ERROR.buildException("Cannot delete customer with active accounts with balance.");
        }
    }
}
