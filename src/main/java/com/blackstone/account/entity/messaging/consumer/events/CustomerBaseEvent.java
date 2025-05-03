package com.blackstone.account.entity.messaging.consumer.events;


import com.blackstone.account.common.annotation.CustomerJsonType;

@CustomerJsonType
public abstract class CustomerBaseEvent {

    public  String getExchange(){
        return "customer-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
