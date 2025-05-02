package com.blackstone.account.messaging.events.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDeleteEvent extends CustomerBaseEvent {
    private String customerId;
    private String customerType;
    @Override
    public String getRoutingKey() {
        return "customer.deleted";
    }

    @Override
    public String getType() {
        return "CUSTOMER_DELETED_EVENT";
    }
}
