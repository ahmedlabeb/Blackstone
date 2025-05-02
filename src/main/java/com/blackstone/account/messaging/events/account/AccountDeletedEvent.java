package com.blackstone.account.messaging.events.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDeletedEvent extends AccountBaseEvent {

    private String accountId;

    @Override
    public String getRoutingKey() {
        return "account.deleted";
    }

    @Override
    public String getType() {
        return "ACCOUNT_DELETED_EVENT";
    }
}
