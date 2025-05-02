
package com.blackstone.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    @Pattern(regexp = "\\d{10}", message = "Account ID must be 10 digits")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String accountId;
    @Pattern(regexp = "\\d{7}", message = "Customer ID must be 7 digits")

    private String customerId;

    @NotNull(message = "Account type is required")
    private AccountType type;
    @NotNull(message = "Account status is required")
    private AccountStatus status;
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", message = "Balance must be non-negative")
    private BigDecimal balance;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
