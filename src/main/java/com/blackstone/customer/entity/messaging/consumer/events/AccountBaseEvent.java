package com.blackstone.customer.entity.messaging.consumer.events;

import com.blackstone.customer.common.annotation.AccountJsonType;

import java.io.Serializable;


@AccountJsonType
public abstract class AccountBaseEvent implements Serializable {

    String getExchange(){
        return "account-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
