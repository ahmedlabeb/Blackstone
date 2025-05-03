package com.blackstone.account.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AccountStatus {

    ACTIVE("active"), INACTIVE("inactive"), CLOSED("closed");
    private final String name;

    AccountStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static AccountStatus forValue(String value) {
        return Arrays.stream(AccountStatus.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account status: " + value));
    }
}
