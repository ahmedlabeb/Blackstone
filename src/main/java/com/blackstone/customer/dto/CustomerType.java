package com.blackstone.customer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CustomerType {
    RETAIL("retail"),CORPORATE("corporate"),INVESTMENT("investment");
    private final String name;

    CustomerType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static CustomerType forValue(String value) {
        return Arrays.stream(CustomerType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer type: " + value));
    }
}
