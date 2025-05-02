package com.blackstone.customer.messaging.events.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent extends AccountBaseEvent{
    private String accountId;

    private String accountType;

    private String customerId;
    @Override
    public String getRoutingKey() {
        return "account.created";
    }

    @Override
    public String getType() {
        return "ACCOUNT_CREATED_EVENT";
    }
}
