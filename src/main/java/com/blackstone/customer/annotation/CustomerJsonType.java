package com.blackstone.customer.annotation;

import com.blackstone.customer.messaging.events.customer.CustomerCreatedEvent;
import com.blackstone.customer.messaging.events.customer.CustomerDeleteEvent;
import com.blackstone.customer.messaging.events.customer.CustomerUpdateEvent;
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
