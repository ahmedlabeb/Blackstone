package com.blackstone.account.messaging.events.customer;


import com.blackstone.account.annotation.CustomerJsonType;

@CustomerJsonType
public abstract class CustomerBaseEvent {

    public  String getExchange(){
        return "customer-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
