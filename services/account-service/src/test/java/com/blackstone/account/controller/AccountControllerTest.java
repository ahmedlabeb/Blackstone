package com.blackstone.account.controller;

import com.blackstone.account.boundry.controller.AccountController;
import com.blackstone.account.entity.dto.AccountDto;
import com.blackstone.account.entity.domain.Account;
import com.blackstone.account.entity.domain.CustomerInfo;
import com.blackstone.account.control.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountDto sampleDto;

    private Account account;

    private CustomerInfo customerInfo;

    @BeforeEach
    public void setup() {
        sampleDto = new AccountDto();
        sampleDto.setAccountId("acc-123");
        sampleDto.setCustomerId("cust-1");
        sampleDto.setBalance(BigDecimal.valueOf(1000));
        customerInfo =new CustomerInfo();
        customerInfo.setCustomerId("cust-1");
        account = new Account();
        account.setAccountId("acc-123");
        account.setCustomerInfo(customerInfo);
        account.setBalance(BigDecimal.valueOf(1000));

    }

    @Test
    public void testCreateAccount_success() throws Exception {
        Mockito.when(accountService.createAccount(any(AccountDto.class))).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc-123"));
    }

    @Test
    public void testCreateAccount_validationError() throws Exception {
        Mockito.when(accountService.createAccount(any())).thenThrow(new RuntimeException("Validation error"));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation error")));
    }

    @Test
    public void testGetAllAccounts_success() throws Exception {
        Mockito.when(accountService.getAllAccounts()).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value("acc-123"));
    }

    @Test
    public void testGetAllAccounts_empty() throws Exception {
        Mockito.when(accountService.getAllAccounts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void testGetAccountById_success() throws Exception {
        Mockito.when(accountService.getAccountById("acc-123")).thenReturn(sampleDto);

        mockMvc.perform(get("/api/accounts/acc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc-123"));
    }

    @Test
    public void testGetAccountById_notFound() throws Exception {
        Mockito.when(accountService.getAccountById("acc-999")).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/accounts/acc-999"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Not found")));
    }

    @Test
    public void testUpdateAccount_success() throws Exception {
        Mockito.when(accountService.updateAccount(eq("acc-123"), any(AccountDto.class))).thenReturn(sampleDto);

        mockMvc.perform(put("/api/accounts/acc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc-123"));
    }

    @Test
    public void testUpdateAccount_validationError() throws Exception {
        Mockito.when(accountService.updateAccount(eq("acc-123"), any(AccountDto.class))).thenThrow(new RuntimeException("Mismatch"));

        mockMvc.perform(put("/api/accounts/acc-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mismatch")));
    }

    @Test
    public void testDeleteAccount_success() throws Exception {
        mockMvc.perform(delete("/api/accounts/acc-123"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAccount_withBalanceError() throws Exception {
        Mockito.doThrow(new RuntimeException("balance error")).when(accountService).deleteAccount("acc-123");

        mockMvc.perform(delete("/api/accounts/acc-123"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("balance error")));
    }

    @Test
    public void testCheckNonZeroBalance_true() throws Exception {
        Mockito.when(accountService.hasNonZeroBalance("cust-1")).thenReturn(true);

        mockMvc.perform(get("/api/accounts/check-balance/cust-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testCheckNonZeroBalance_false() throws Exception {
        Mockito.when(accountService.hasNonZeroBalance("cust-1")).thenReturn(false);

        mockMvc.perform(get("/api/accounts/check-balance/cust-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void testCheckNonZeroBalance_exception() throws Exception {
        Mockito.when(accountService.hasNonZeroBalance("cust-1")).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/accounts/check-balance/cust-1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }
}

