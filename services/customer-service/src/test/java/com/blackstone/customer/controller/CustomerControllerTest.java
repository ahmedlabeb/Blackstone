package com.blackstone.customer.controller;

import com.blackstone.customer.boundry.controller.CustomerController;
import com.blackstone.customer.control.service.CustomerService;
import com.blackstone.customer.entity.dto.CustomerDto;
import com.blackstone.customer.entity.dto.CustomerType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setCustomerId("1234567");
        customerDto.setName("John Doe");
        customerDto.setLegalId("LEGAL123");
        customerDto.setAddress("123 Street, City");
        customerDto.setType(CustomerType.RETAIL);
        customerDto.setDeleted(false);
    }

    @Test
    void testCreateCustomer_success() throws Exception {
        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1234567"));
    }

    @Test
    void testGetCustomer_success() throws Exception {
        when(customerService.getCustomer("1234567")).thenReturn(customerDto);

        mockMvc.perform(get("/api/customers/1234567"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1234567"));
    }

    @Test
    void testGetAllCustomers_success() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customerDto));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("1234567"));
    }

    @Test
    void testUpdateCustomer_success() throws Exception {
        when(customerService.updateCustomer(any(String.class), any(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(put("/api/customers/1234567")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1234567"));
    }

    @Test
    void testDeleteCustomer_success() throws Exception {
        mockMvc.perform(delete("/api/customers/1234567"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateCustomer_validationFail() throws Exception {
        CustomerDto invalidDto = new CustomerDto();

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testCreateCustomer_missingRequiredFields() throws Exception {
        CustomerDto missingFieldsDto = new CustomerDto();
        missingFieldsDto.setCustomerId("1234567"); // read-only, so ignored

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingFieldsDto)))
                .andExpect(status().isBadRequest());
    }
}
