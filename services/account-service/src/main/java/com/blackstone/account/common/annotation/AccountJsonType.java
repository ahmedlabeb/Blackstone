package com.blackstone.account.common.annotation;

import com.blackstone.account.entity.messaging.producer.events.AccountCreatedEvent;
import com.blackstone.account.entity.messaging.producer.events.AccountDeletedEvent;
import com.blackstone.account.entity.messaging.producer.events.AccountUpdatedEvent;
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
        @JsonSubTypes.Type(value = AccountCreatedEvent.class, name = "AccountCreatedEvent"),
        @JsonSubTypes.Type(value = AccountUpdatedEvent.class, name = "AccountUpdatedEvent"),
        @JsonSubTypes.Type(value = AccountDeletedEvent.class, name = "AccountDeletedEvent")
    })
@JacksonAnnotationsInside
public @interface AccountJsonType {
}
