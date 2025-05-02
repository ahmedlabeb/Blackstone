package com.blackstone.account.annotation;

import com.blackstone.account.messaging.events.account.AccountBaseEvent;
import com.blackstone.account.messaging.events.account.AccountDeletedEvent;
import com.blackstone.account.messaging.events.account.AccountUpdatedEvent;
import com.blackstone.account.messaging.events.customer.CustomerCreatedEvent;
import com.blackstone.account.messaging.events.customer.CustomerDeleteEvent;
import com.blackstone.account.messaging.events.customer.CustomerUpdateEvent;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = CustomerCreatedEvent.class, name = "CustomerCreatedEvent"),
        @JsonSubTypes.Type(value = CustomerUpdateEvent.class, name = "CustomerUpdateEvent"),
        @JsonSubTypes.Type(value = CustomerDeleteEvent.class, name = "CustomerDeleteEvent")
    })
@JacksonAnnotationsInside
public @interface CustomerJsonType {
}
