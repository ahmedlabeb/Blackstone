package com.blackstone.account.entity.messaging.consumer.events;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = CustomerEventMessage.class)
public class CustomerEventMessage implements Serializable {

    @JsonProperty("customer_event")
    private CustomerBaseEvent customerBaseEvent;
}
