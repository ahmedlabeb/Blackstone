package com.blackstone.customer.messaging.events.customer;


import com.blackstone.customer.annotation.CustomerJsonType;

@CustomerJsonType
public abstract class CustomerBaseEvent {

    public  String getExchange(){
        return "customer-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
