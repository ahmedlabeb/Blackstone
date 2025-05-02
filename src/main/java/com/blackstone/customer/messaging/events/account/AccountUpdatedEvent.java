package com.blackstone.customer.messaging.events.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdatedEvent extends AccountBaseEvent {

    private String accountId;

    private String accountType;

    private String customerId;

    @Override
    public String getRoutingKey() {
        return "account.updated";
    }

    @Override
    public String getType() {
        return "ACCOUNT_UPDATED_EVENT";
    }
}
