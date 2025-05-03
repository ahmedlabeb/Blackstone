
package com.blackstone.customer.entity.messaging.producer.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent extends CustomerBaseEvent {
    private String customerId;
    private String customerType;

    @Override
    public String getRoutingKey() {
        return "customer.created";
    }

    @Override
    public String getType() {
        return "CUSTOMER_CREATED_EVENT";
    }
}
