package com.blackstone.customer.messaging.events.account;

import com.blackstone.customer.annotation.AccountJsonType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@AccountJsonType
public abstract class AccountBaseEvent implements Serializable {

    String getExchange(){
        return "account-events";
    }
    public abstract String  getRoutingKey();

    public abstract String getType();
}
