
package com.blackstone.customer.messaging.events.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateEvent extends CustomerBaseEvent {
    private String customerId;
    private String customerType;

    @Override
    public String getRoutingKey() {
        return "customer.updated";
    }

    @Override
    public String getType() {
        return "CUSTOMER_UPDATED_EVENT";
    }
}
