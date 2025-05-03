package com.blackstone.customer.common.annotation;

import com.blackstone.customer.entity.messaging.producer.events.CustomerCreatedEvent;
import com.blackstone.customer.entity.messaging.producer.events.CustomerDeleteEvent;
import com.blackstone.customer.entity.messaging.producer.events.CustomerUpdateEvent;
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
