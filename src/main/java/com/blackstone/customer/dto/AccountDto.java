
package com.blackstone.customer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String accountId;
    private String customerId;
    private String status;
    private BigDecimal balance;
}
