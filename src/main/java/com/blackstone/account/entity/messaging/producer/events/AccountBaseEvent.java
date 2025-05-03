package com.blackstone.account.entity.messaging.producer.events;


import com.blackstone.account.common.annotation.AccountJsonType;

import java.io.Serializable;


@AccountJsonType
public abstract class AccountBaseEvent implements Serializable {

    public String getExchange(){
        return "account-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
