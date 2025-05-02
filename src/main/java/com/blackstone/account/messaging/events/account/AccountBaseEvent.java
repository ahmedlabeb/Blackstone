package com.blackstone.account.messaging.events.account;


import com.blackstone.account.annotation.AccountJsonType;

import java.io.Serializable;


@AccountJsonType
public abstract class AccountBaseEvent implements Serializable {

    public String getExchange(){
        return "account-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
