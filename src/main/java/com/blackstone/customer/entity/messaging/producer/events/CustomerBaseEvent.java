package com.blackstone.customer.entity.messaging.producer.events;


import com.blackstone.customer.common.annotation.CustomerJsonType;

@CustomerJsonType
public abstract class CustomerBaseEvent {

    public  String getExchange(){
        return "customer-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
